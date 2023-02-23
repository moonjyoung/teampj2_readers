package com.readers.be3.service;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.ArticleCommentEntity;
import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.entity.SearchArticleView;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.ArticleImgEntity;
import com.readers.be3.repository.ArticleCommentRepository;
import com.readers.be3.repository.ArticleInfoRepository;
import com.readers.be3.repository.SearchArticleViewRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.image.ArticleImgRepository;
import com.readers.be3.utilities.RandomNameUtils;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.GetSearchArticleVO;
import com.readers.be3.vo.article.PostArticleVO;
import com.readers.be3.vo.article.PostWriterCommentVO;
import com.readers.be3.vo.article.SearchNicknameVO;

@Service
public class ArticleService {
    @Autowired ArticleImgRepository articleImgRepo;
    @Autowired ArticleInfoRepository articleInfoRepo;
    @Autowired UserInfoRepository userInfoRepo;
    @Autowired SearchArticleViewRepository searchArticleRepo;
    @Autowired ArticleCommentRepository ArticleCommentRepo;
    @Value("${file.image.article}") String ArticleImgPath;

    // 게시글 작성 
    public Map<String, Object> writeArticle(PostArticleVO data){
        // VO를 통해 게시글 제목과 내용, 파일(이미지)을 입력받음
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        ArticleInfoEntity articleInfoEntity = null;
        ArticleImgEntity articleImgEntity = null;
        // 제목을 입력하지 않았다면 입력하게 처리
        if(data.getAiTitle() == null){
            resultMap.put("status", false);
            resultMap.put("message", "제목을 입력하세요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(data.getAiContent() == null){
            resultMap.put("status", false);
            resultMap.put("message", "내용을 입력하세요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(data.getAiPublic() == null){
            resultMap.put("status", false);
            resultMap.put("message", "공개여부를 선택하세요(1.공개 / 2.비공개).");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        // 유효성검사를 다 통과했다면
        else{
            // 게시글 저장
            articleInfoEntity = ArticleInfoEntity.builder()
                                .aiTitle(data.getAiTitle())
                                .aiContent(data.getAiContent())
                                .aiPublic(data.getAiPublic())
                                .aiUiSeq(data.getUiSeq())
                                .aiBiSeq(data.getBiSeq())
                                .build();
            articleInfoRepo.save(articleInfoEntity);
            // 이미지 저장
            try{
                imgfileHandler(data.getFiles(), articleInfoEntity.getAiSeq());
                resultMap.put("messageFile", "업로드 성공");
            }
            catch(Exception e){
                resultMap.put("messageFile", "업로드 실패");

            e.printStackTrace();
            }

            resultMap.put("status", true);
            resultMap.put("message", "게시글이 등록되었습니다.");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    // 이미지 파일 저장 메소드
   public void imgfileHandler(List<MultipartFile> files, Long aiSeq) throws Exception {
    if (!CollectionUtils.isEmpty(files)) {
        for (MultipartFile multipartFile : files) {
            if (!multipartFile.isEmpty()) { // 파일이 비어있지 않다면
                String contentType = multipartFile.getContentType();
                String originalFileExtension = "";

                if (ObjectUtils.isEmpty(contentType)) { // 확장자명이 없다면(잘못된 파일)
                    break;
                } else {
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = "jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = "png";
                    } else if (contentType.contains("image/gif")) {
                        originalFileExtension = "gif";
                    } else {
                        break;
                    }
                }

                String newFileName = "article_" + Calendar.getInstance().getTimeInMillis() + "." + originalFileExtension;
                ArticleImgEntity articleImgEntity = ArticleImgEntity.builder()
                        .aimgFilename(newFileName)
                        .aimgAiSeq(aiSeq)
                        .aimgUri(RandomNameUtils.MakeRandomUri(contentType, aiSeq))
                        .build();
                articleImgRepo.save(articleImgEntity);

                File file = new File(ArticleImgPath);
                if (!file.exists()) { // 저장할 위치의 디렉토리가 존재하지 않을 경우
                    file.mkdirs(); // 디렉토리를 생성
                }
               Path savePath = Paths.get(ArticleImgPath+File.separator+newFileName);
                multipartFile.transferTo(savePath);
            }
        }
        System.out.println("이미지가 저장되었습니다.");
    } else {
        System.out.println("등록한 이미지가 없습니다.");
    }
}

// 게시글 조회
// 검색(작성자, 제목, 내용)
// pathvarible 로 검색타입(모든 게시글, 작성자, 제목, 내용 )
// type => (all, writer, title, content)

public Map<String, Object> getArticleList(String type, String keyword, Pageable pageable){
Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

if(type.equals("all")){
    // Page<ArticleInfoEntity> page = articleInfoRepo.findAllAndAiPublicContains(1,pageable);
    // 게시글 조회 api , 게시글 보기 api 따로 작성
    Page<GetSearchArticleVO> page = articleInfoRepo.findAllArtilce(pageable);
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "전체 게시글 리스트 조회.");
    resultMap.put("code", HttpStatus.OK);
}
else if(type.equals("writer")){
    Page<SearchNicknameVO> page = searchArticleRepo.searchNickname(keyword, pageable);
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "닉네임으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
}
else if(type.equals("title")){
    Page<GetSearchArticleVO> page = articleInfoRepo.findByAiTitleContains(keyword, pageable);
    
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "제목으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
}
else if(type.equals("content")){
    Page<GetSearchArticleVO> page = articleInfoRepo.findByAiContentContains(keyword, pageable);

    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "내용으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
}
else{
    resultMap.put("status", false);
    resultMap.put("message", "잘못된 검색 타입이에요 (type = (all, writer, title, content)).");
    resultMap.put("code", HttpStatus.BAD_REQUEST);
}
return resultMap;
}

// 게시글 상세조회
public Map<String, Object> getArticleDetailInfo(Long aiSeq){
    Map<String, Object> resultMap = new HashMap<>();
    ArticleInfoEntity detailInfo = articleInfoRepo.findByAiSeq(aiSeq);
    if(detailInfo.getAiPublic() == 2){
        resultMap.put("status", false);
        resultMap.put("message", "비공개된 게시글이에요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
    else if(detailInfo.getAiStatus() == 2){
        resultMap.put("status", false);
        resultMap.put("message", "삭제된 게시글이에요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
    else{
        resultMap.put("data", detailInfo);
        resultMap.put("status", true);
        resultMap.put("message", "게시글 상세보기.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
    

    return resultMap;
}

// 게시글 수정
public Map<String, Object> modifyArticle(Long uiSeq, Long aiSeq, ArticleModifyVO data){
    Map<String, Object> resultMap = new HashMap<>();
    ArticleInfoEntity modifyPost = null;
    
    // 수정할 게시글을 선택
    modifyPost = articleInfoRepo.findByAiSeq(aiSeq);
    // 수정일
    LocalDateTime modifyDate = LocalDateTime.now();
    if(modifyPost == null){
        resultMap.put("status", false);
        resultMap.put("message", "게시글이 존재하지 않습니다.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(modifyPost.getAiUiSeq() != uiSeq.intValue()){
        resultMap.put("status", false);
        resultMap.put("message", "다른사람의 게시글은 수정할 수 없어요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(modifyPost.getAiStatus() == 2){
        resultMap.put("status", false);
        resultMap.put("message", "삭제된 게시글 입니다.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    // 유효성 검사를 통과했다면
    else{
        if (data.getAiTitle() != null) {
            modifyPost.setAiTitle(data.getAiTitle());
        }
        if (data.getAiContent() != null) {
            modifyPost.setAiContent(data.getAiContent());
        }
        if (data.getAiPublic() != null) {
            modifyPost.setAiPublic(data.getAiPublic());
        }
        if (!data.getFiles().isEmpty()){
        // 
        try{
            imgfileHandler(data.getFiles(), aiSeq);
            resultMap.put("messageFile", "업로드 성공");
        }
        catch(Exception e){
            resultMap.put("messageFile", "업로드 실패");
            e.printStackTrace();
        }
        }
        modifyPost.setAiModDt(modifyDate);
        articleInfoRepo.save(modifyPost);
        resultMap.put("status", true);
        resultMap.put("message", "수정되었습니다.");
        resultMap.put("code", HttpStatus.OK);
    }
    return resultMap;
}

// 게시글 삭제
public Map<String, Object> deleteArticle(Long uiSeq, Long aiSeq){
    Map<String, Object> resultMap = new HashMap<>();
    ArticleInfoEntity deletePost = null;
    deletePost = articleInfoRepo.findByAiSeq(aiSeq);
    if(Objects.isNull(deletePost)){
        resultMap.put("status", false);
        resultMap.put("message", "선택한 게시글이 존재하지 않아요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(deletePost.getAiUiSeq() != uiSeq.intValue()){
        resultMap.put("status", false);
        resultMap.put("message", "다른사람의 게시글은 삭제할 수 없어요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else{
        deletePost.setAiStatus(2);
        articleInfoRepo.save(deletePost);
        resultMap.put("status", true);
        resultMap.put("message", "삭제되었어요.");
        resultMap.put("code", HttpStatus.OK);
    }
    return resultMap;
}

//댓글 작성
public Map<String, Object> postWriteComment(PostWriterCommentVO data){
    Map<String, Object> resultMap = new HashMap<>();
    
    if(data.getAcContent() == null){
        resultMap.put("status", false);
        resultMap.put("message", "내용을 입력하세요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(data.getAcAiSeq() == null){
        resultMap.put("status", false);
        resultMap.put("message", "한줄평을 달 게시글을 선택해주세요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    if(data.getAcUiSeq() == null){
        resultMap.put("status", false);
        resultMap.put("message", "로그인을 해주세요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else{
        ArticleCommentEntity comment = ArticleCommentEntity.builder()
                                        .acContent(data.getAcContent())
                                        .acAiSeq(data.getAcAiSeq())
                                        .acUiSeq(data.getAcUiSeq())
                                        .build();
                                        ArticleCommentRepo.save(comment);
        resultMap.put("status", true);
        resultMap.put("message", "댓글이 등록되었습니다..");
        resultMap.put("code", HttpStatus.OK);
    }

    return resultMap;

}
}