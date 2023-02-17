package com.readers.be3.service;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.ArticleImgEntity;
import com.readers.be3.repository.ArticleInfoRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.image.ArticleImgRepository;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.WriteArticleVO;

@Service
public class ArticleService {
    @Autowired ArticleImgRepository articleImgRepo;
    @Autowired ArticleInfoRepository articleInfoRepo;
    @Autowired UserInfoRepository userInfoRepo;
    @Value("${file.image.article}") String ArticleImgPath;

    // 독후감 작성 기능
    public Map<String, Object> writeArticle(WriteArticleVO data, List<MultipartFile> files){
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
                // imgfileHandler(files, articleInfoEntity);
                imgfileHandler(files, articleInfoEntity.getAiSeq());
            }
            catch(Exception e){
            e.printStackTrace();
            }

            resultMap.put("status", true);
            resultMap.put("message", "게시글이 등록되었습니다.");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    // 사용자로부터 이미지파일을 받아서 articleImgeEntity 에 저장 
    // public void imgfileHandler(List<MultipartFile> files, ArticleInfoEntity article)throws Exception {
    public void imgfileHandler(List<MultipartFile> files, Long aiSeq)throws Exception {
        // 반환할 파일 리스트
        // List<ArticleImgEntity> fileList = new ArrayList<>();
        
        // 비어있는 파일이 들어오면 빈 것을 반환
        // * collectionUtils.isEmpty(xxxList) => list타입 데이터가 비어있는지 확인하기 위해 사용(Apache Commons 라이브러리로서 null체크를 자동으로 해줌(NPE 방지))
        if(!(CollectionUtils.isEmpty(files))){
        // 파일 이름을 업로드한 날짜로 바꾸어서 저장(why? 파일명이 중복되면 나중에 업로드하는 파일이 덮어쓰게 됨 => UUID적용하거나 파일이름뒤 날짜나 숫자를 붙임)
        // 여기서는 파일 이름을 업로드한 날짜로 적용
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = SimpleDateFormat.format(new Date());


        // 파일이 저장될 경로 지정
        String path = ArticleImgPath;

        // 파일 hendler
        for(MultipartFile multipartFile : files){
            // 파일이 비어있지 않다면
            if(!CollectionUtils.isEmpty(files)){
                String contentType = multipartFile.getContentType();
                String originalFileExtension;
                // 확장자명이 없다면(잘못된 파일)
                if(ObjectUtils.isEmpty(contentType)){
                    break;
                }
                else{
                    if(contentType.contains("image/jpeg")){
                        originalFileExtension = ".jpg";
                    }
                    else if(contentType.contains("image/png")){
                        originalFileExtension = ".png";
                    }
                    else if(contentType.contains("image/gif")){
                        originalFileExtension = ".gif";
                    }
                    // 다른 파일 명이면 아무일 없음
                    else{
                        break;
                    }
                }
                // 파일 이름 설정(나노초단위로)
                String newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

                // img파일 entity에 저장
                ArticleImgEntity articleImgEntity = ArticleImgEntity.builder() 
                                            .aimgFilename(newFileName)
                                            // .article(article)
                                            .aimgAiSeq(aiSeq)
                                            .aimgUri(path)
                                            .build();
                articleImgRepo.save(articleImgEntity);
                
                // path 경로를 갖는 file 객체를 생성
                File file = new File(path);
                // 저장할 위치의 디렉토리가 존재하지 않을 경우
                if(!file.exists()){
                    file.mkdirs();
                }
                // 실제 파일 저장
                multipartFile.transferTo(file);; 
            }
        } // for문 끝
        System.out.println("이미지가 저장되었어요.");
    }
    else{
        System.out.println("등록한 이미지가 없어요.");
    }

    }

// 게시글 조회
// 검색(작성자, 제목, 내용)
// pathvarible 로 검색타입(모든 게시글, 작성자, 제목, 내용 )
// type => (all, writer, title, content)

public Map<String, Object> getArticleList(String type, String keyword, Pageable pageable){
Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

if(type.equals("all")){
    Page<ArticleInfoEntity> page = articleInfoRepo.findAll(pageable);
    List<ArticleInfoEntity> status = articleInfoRepo.findAll();
    
    // ai_public 이 1일때만 보이게 (aiPublic를 어디서 가져오지?)
    // 게시글 조회 api , 게시글 보기 api 따로 만들어야 하나?
    // 
    page.getContent();
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "전체 게시글 리스트 조회.");
    resultMap.put("code", HttpStatus.OK);
}
else if(type.equals("writer")){
    // 유저 아이디 검색해서 해당하는 유저정보 가져오기
    UserInfoEntity writerInfo = userInfoRepo.findByUiNickname(keyword);
    if(writerInfo == null){
    resultMap.put("status", false);
    resultMap.put("message", "해당하는 유저가 없습니다.");
    resultMap.put("code", HttpStatus.BAD_REQUEST);
    // 검색한 유저정보에서 유저seq로 작성한 게시글 검색
    }
    else{
    Page<ArticleInfoEntity> page = articleInfoRepo.findByAiUiSeq(writerInfo.getUiSeq(), pageable);
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "닉네임으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
    }
}
else if(type.equals("title")){
    Page<ArticleInfoEntity> page = articleInfoRepo.findByAiTitleContains(keyword, pageable);
    
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "제목으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
}
else if(type.equals("content")){
    Page<ArticleInfoEntity> page = articleInfoRepo.findByAiContentContains(keyword, pageable);

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

// 게시글 수정
// 회원이 작성한 게시글 목록 중에서 수정할 게시글 선택
// 
// uiSeq => 로그인 상태 확인을 위해서 받아옴
// 
public Map<String, Object> modifyArticle(Long uiSeq, Long aiSeq, ArticleModifyVO data){
    Map<String, Object> resultMap = new HashMap<>();
    ArticleInfoEntity modifyPost = null;
    // 로그인한 회원이 작성한 게시글 목록을 modifyPost에 담음
    // modifyPost = articleInfoRepo.findByAiUiSeq(uiSeq);
    
    // 본인이 작성한 게시글 만 수정할 수 있게 만들어야 함
    // 수정할 게시글을 선택했을때 => aiSeq를 매개변수로 받음
    // 그 게시글이 본인이 작성한 게시글인지 확인하고 =>
    // 본인이 작성한 게시글이라면 수정가능하게 setter사용해서 entity에 값 주입

    // 
    // for(ArticleInfoEntity writerSeq : modifyPost){
        // 
    // }
    
    
    
    if(uiSeq == null){
        resultMap.put("status", false);
        resultMap.put("message", "로그인을 해주세요");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    // 로그인된 상태라면
    else{
    // 수정할 게시글을 선택
    
        if (data.getAiTitle() != null) {
            modifyPost.setAiTitle(data.getAiTitle());
        }
        if (data.getAiContent() != null) {
            modifyPost.setAiContent(data.getAiContent());
        }
        if (data.getAiModDt() != null) {
            modifyPost.setAiModDt(data.getAiModDt());
        }
        if (data.getAiPublic() != null) {
            modifyPost.setAiPublic(data.getAiPublic());
        }
        else if(data.getAiTitle() == null){
            resultMap.put("status", false);
            resultMap.put("message", "제목을 입력하세요");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(data.getAiContent() == null){
            resultMap.put("status", false);
            resultMap.put("message", "내용을 입력하세요");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        articleInfoRepo.save(modifyPost);
        resultMap.put("status", true);
        resultMap.put("message", "수정되었습니다.");
        resultMap.put("code", HttpStatus.OK);
    }   
    return resultMap;
}

}
