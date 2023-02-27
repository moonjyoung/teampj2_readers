package com.readers.be3.service;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Calendar;
=======
import java.util.Calendar;
import java.util.Date;
>>>>>>> rsj6
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.readers.be3.exception.ErrorResponse;
<<<<<<< HEAD
=======

>>>>>>> rsj6
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.type.descriptor.java.LocalDateJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.ArticleCommentEntity;
import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.ArticleImgEntity;
import com.readers.be3.exception.ReadersProjectException;
import com.readers.be3.repository.ArticleCommentRepository;
import com.readers.be3.repository.ArticleInfoRepository;
import com.readers.be3.repository.SearchArticleViewRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.image.ArticleImgRepository;
import com.readers.be3.utilities.RandomNameUtils;
import com.readers.be3.vo.article.ArticleDetailVO;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.GetCommentVO;
import com.readers.be3.vo.article.GetImgInfoVO;
<<<<<<< HEAD
import com.readers.be3.vo.article.GetSearchArticleVO;
import com.readers.be3.vo.article.PatchCommentVO;
import com.readers.be3.vo.article.PostArticleVO;
import com.readers.be3.vo.article.PostWriterCommentVO;
import com.readers.be3.vo.article.SearchNicknameVO;
import com.readers.be3.vo.article.responseVO.ArticleResponseVO;
=======
import com.readers.be3.vo.article.PatchCommentVO;
import com.readers.be3.vo.article.PostArticleVO;
import com.readers.be3.vo.article.PostWriterCommentVO;
import com.readers.be3.vo.article.response.ArticleModifyResponse;
import com.readers.be3.vo.article.response.ArticleSearchResponseVO;
import com.readers.be3.vo.article.response.CommentResponse;
import com.readers.be3.vo.article.response.ResponseMessageVO;
import com.readers.be3.vo.article.response.WriteArticleResponseVO;
>>>>>>> rsj6

@Service
public class ArticleService {
    @Autowired ArticleImgRepository articleImgRepo;
    @Autowired ArticleInfoRepository articleInfoRepo;
    @Autowired UserInfoRepository userInfoRepo;
    @Autowired SearchArticleViewRepository searchArticleRepo;
    @Autowired ArticleCommentRepository ArticleCommentRepo;
<<<<<<< HEAD
=======
    
>>>>>>> rsj6

    @Value("${file.image.article}") String ArticleImgPath;

    // 게시글 작성 
<<<<<<< HEAD
    public ArticleInfoEntity writeArticle(PostArticleVO data){
        PostArticleVO response = new PostArticleVO();
=======
    public WriteArticleResponseVO writeArticle(PostArticleVO data){
>>>>>>> rsj6
        // VO를 통해 게시글 제목과 내용, 파일(이미지)을 입력받음
        ArticleInfoEntity articleInfoEntity = null;
        WriteArticleResponseVO response = null;
        UserInfoEntity user = userInfoRepo.findByUiSeq(Long.valueOf(data.getUiSeq()));
        // 제목을 입력하지 않았다면 입력하게 처리
<<<<<<< HEAD
        if(data.getAiTitle() == null){
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   제목을 입력하세요")));
            // response = ArticleResponseVO.builder()
            // .status(false)
            // .message("제목을 입력하세요")
            // .code(HttpStatus.BAD_REQUEST)
            // .build();
        }
        else if(data.getContent() == null){
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   내용을 입력하세요")));
//            response = ArticleResponseVO.builder()
//            .status(false)
//            .message("내용을 입력하세요")
//            .code(HttpStatus.BAD_REQUEST)
//            .build();
        }
        else if(data.getAiPublic() == null){
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND,String.format("%s   공개여부를 선택하세요(1.공개 / 2.비공개)")));
//            response = ArticleResponseVO.builder()
//            .status(false)
//            .message("공개여부를 선택하세요(1.공개 / 2.비공개)")
//            .code(HttpStatus.BAD_REQUEST)
//            .build();
        }
        // 유효성검사를 다 통과했다면
=======
        if(user == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("로그인을 해주세요.")));
        else if(data.getAiTitle() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("제목을 입력하세요")));
        
        else if(data.getContent() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("내용을 입력하세요")));
        
        else if(data.getAiPublic() == null)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("공개여부를 선택하세요(1.공개 / 2.비공개)")));

>>>>>>> rsj6
        else{
            // 게시글 저장
            articleInfoEntity = ArticleInfoEntity.builder()
                                .aiTitle(data.getAiTitle())
                                .aiContent(data.getContent())
                                .aiPublic(data.getAiPublic())
                                .aiUiSeq(data.getUiSeq())
                                .aiBiSeq(data.getBiSeq())
                                .aiRegDt(LocalDateTime.now())
                                .aiPurpose(1)
                                .aiStatus(1)
                                .build();
            articleInfoRepo.save(articleInfoEntity);
            // 이미지 저장
            try{
                imgfileHandler(data.getFiles(), articleInfoEntity.getAiSeq());
<<<<<<< HEAD
                // resultMap.put("messageFile", "업로드 성공");
            }
            catch(Exception e){
                // resultMap.put("messageFile", "업로드 실패");
            e.printStackTrace();
            }
//            response = ArticleResponseVO.builder()
//            .status(true)
//            .message("게시글이 등록되었습니다.")
//            .code(HttpStatus.OK)
//            .build();
        }
        return articleInfoEntity;
=======
            }
            catch(Exception e){
            e.printStackTrace();
            }
            List<GetImgInfoVO> imgFiles = articleImgRepo.findByAimgAiSeq(articleInfoEntity.getAiSeq());
            // ResponseMessageVO 
            response = WriteArticleResponseVO.builder()
                                .aiSeq(articleInfoEntity.getAiSeq())
                                .aiTitle(articleInfoEntity.getAiTitle())
                                .aiContent(articleInfoEntity.getAiContent())
                                .aiPublic(articleInfoEntity.getAiPublic())
                                .aiUiSeq(articleInfoEntity.getAiUiSeq())
                                .aiBiSeq(articleInfoEntity.getAiBiSeq())
                                .aiRegDt(articleInfoEntity.getAiRegDt())
                                .aiPurpose(articleInfoEntity.getAiPurpose())
                                .aiStatus(articleInfoEntity.getAiStatus())
                                .imgFiles(imgFiles)
                                .build();
        }
        return response;
>>>>>>> rsj6
    }

    // 이미지 파일 저장 메소드
   public void imgfileHandler(List<MultipartFile> files, Long aiSeq) throws Exception {
    if (!CollectionUtils.isEmpty(files)) {
        for (MultipartFile multipartFile : files) {
            if (!multipartFile.isEmpty()) { // 파일이 비어있지 않다면
                String contentType = multipartFile.getContentType();
                String originalFileExtension = "";

                if (ObjectUtils.isEmpty(contentType))  // 확장자명이 없다면(잘못된 파일)
                    break;
<<<<<<< HEAD
                } else {
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = "jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = "png";
                    } else if (contentType.contains("image/gif")) {
                        originalFileExtension = "gif";
                    } else {
                        break;
=======
                 else {
                    if      (contentType.contains("image/jpeg"))    originalFileExtension = "jpg";
                    else if (contentType.contains("image/png"))     originalFileExtension = "png";
                    else if (contentType.contains("image/gif"))     originalFileExtension = "gif";
                    else  break;
>>>>>>> rsj6
                    }

                String newFileName = "article_" + Calendar.getInstance().getTimeInMillis() + "." + originalFileExtension;
                ArticleImgEntity articleImgEntity = ArticleImgEntity.builder()
                        .aimgFilename(newFileName)
                        .aimgAiSeq(aiSeq)
                        .aimgUri(RandomNameUtils.MakeRandomUri(originalFileExtension, aiSeq))
                        .build();
                articleImgRepo.save(articleImgEntity);

                File file = new File(ArticleImgPath);
                // 저장할 위치의 디렉토리가 존재하지 않을 경우 디렉토리를 생성
                if (!file.exists())  file.mkdirs(); 
                
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

<<<<<<< HEAD
public List<GetSearchArticleVO> getArticleList(String type, String keyword, Pageable pageable){
Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
// Page<GetSearchArticleVO> response = null;
List<GetSearchArticleVO> resultList = new ArrayList<>(); // 내보낼 데이터
Page<GetSearchArticleVO> searchRespon = articleInfoRepo.findAllArtilce(pageable);
if(type.equals("all")){
    // Page<ArticleInfoEntity> page = articleInfoRepo.findAllAndAiPublicContains(1,pageable);
    // 게시글 조회 api , 게시글 보기 api 따로 작성
    
    resultMap.put("data", searchRespon);
    resultMap.put("totalPage", searchRespon.getTotalPages()); // 총 페이지 수
    resultMap.put("totalData", searchRespon.getTotalElements()); // 총 데이터 수
    resultMap.put("currentPage", searchRespon.getNumber()); // 현재 페이지 번호
    resultMap.put("status", true);
    resultMap.put("message", "전체 게시글 리스트 조회.");
    resultMap.put("code", HttpStatus.OK);
=======
public List<ArticleSearchResponseVO> getArticleList(String type, String keyword, Integer page ,Integer size ){
    List<ArticleSearchResponseVO> response = null;
    if(page == null) page = 0;
    if(size == null) size = 10;
    PageRequest pageRequest = PageRequest.of(page,size,Sort.by("aiRegDt").descending());

    // 게시글 전체 검색
if(type.equals("all")){
     response = searchArticleRepo.findAll(pageRequest);
>>>>>>> rsj6
}
    // 작성자로 검색(닉네임)
else if(type.equals("writer")){
<<<<<<< HEAD
    Page<SearchNicknameVO> searchResponse = searchArticleRepo.searchNickname(keyword, pageable);
    
    for(SearchNicknameVO vo : searchResponse.getContent()) {
        GetSearchArticleVO articleVO = GetSearchArticleVO.builder()
            .aiSeq(vo.getAiSeq())
            .aiTitle(vo.getAiTitle())
            .aiPurpose(vo.getAiPurpose())
            .uiNickname(vo.getUiNickname())
            .aiUiSeq(vo.getAiUiSeq())
            .aiBiSeq(vo.getAiBiSeq())
            .aiStatus(vo.getAiStatus())
            .build();
        resultList.add(articleVO);
    }
    // resultMap.put("data", searchRespon);
    resultMap.put("data", resultList);
    resultMap.put("status", true);
    resultMap.put("message", "닉네임으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
=======
     response = searchArticleRepo.searchNickname(keyword, pageRequest);
>>>>>>> rsj6
}
 // 제목으로 검색
else if(type.equals("title")){
<<<<<<< HEAD
    Page<GetSearchArticleVO> searchResponse = articleInfoRepo.findByAiTitleContains(keyword, pageable);
    for(GetSearchArticleVO vo : searchRespon.getContent()) {
        GetSearchArticleVO articleVO = GetSearchArticleVO.builder()
            .aiSeq(vo.getAiSeq())
            .aiTitle(vo.getAiTitle())
            .aiPurpose(vo.getAiPurpose())
            .uiNickname(vo.getUiNickname())
            .aiUiSeq(vo.getAiUiSeq())
            .aiBiSeq(vo.getAiBiSeq())
            .aiStatus(vo.getAiStatus())
            .build();
        resultList.add(articleVO);
    }
    resultMap.put("data", searchRespon);
    resultMap.put("status", true);
    resultMap.put("message", "제목으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
}
else if(type.equals("content")){
    Page<GetSearchArticleVO> searchResponse = articleInfoRepo.findByAiContentContains(keyword, pageable);
    for(GetSearchArticleVO vo : searchRespon.getContent()) {
        GetSearchArticleVO articleVO = GetSearchArticleVO.builder()
            .aiSeq(vo.getAiSeq())
            .aiTitle(vo.getAiTitle())
            .aiPurpose(vo.getAiPurpose())
            .uiNickname(vo.getUiNickname())
            .aiUiSeq(vo.getAiUiSeq())
            .aiBiSeq(vo.getAiBiSeq())
            .aiStatus(vo.getAiStatus())
            .build();
        resultList.add(articleVO);
    }
    resultMap.put("data", searchRespon);
    resultMap.put("status", true);
    resultMap.put("message", "내용으로 검색(검색어 :" + keyword +").");
    resultMap.put("code", HttpStatus.OK);
=======
     response = searchArticleRepo.searchTitle(keyword, pageRequest);

>>>>>>> rsj6
}
 // 내용으로 검색
else if(type.equals("content")){
     response = searchArticleRepo.searchContent(keyword, pageRequest);
    }
else{
<<<<<<< HEAD
   
    resultMap.put("status", false);
    resultMap.put("message", "잘못된 검색 타입이에요 (type = (all, writer, title, content)).");
    resultMap.put("code", HttpStatus.BAD_REQUEST);
}
return resultList;
=======
    throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("%s 잘못된 검색타입 입니다.")));
}
return response;
>>>>>>> rsj6
}

// 게시글 상세조회
public ArticleDetailVO getArticleDetailInfo(Long aiSeq){
<<<<<<< HEAD
    Map<String, Object> resultMap = new HashMap<>();
=======
>>>>>>> rsj6
    ArticleInfoEntity detailInfo = articleInfoRepo.findByAiSeq(aiSeq);
    List <GetCommentVO> showComment = ArticleCommentRepo.findByAcAiSeqAndAcStatus(aiSeq, 1);
    List <GetImgInfoVO> showImgInfo = articleImgRepo.findByAimgAiSeq(aiSeq);
    ArticleDetailVO response = null;
<<<<<<< HEAD
    if(detailInfo == null){
        
        resultMap.put("status", false);
        resultMap.put("message", "비공개된 게시글이에요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(detailInfo.getAiPublic() == 2){
        resultMap.put("status", false);
        resultMap.put("message", "비공개된 게시글이에요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
    else if(detailInfo.getAiStatus() == 2){
        resultMap.put("status", false);
        resultMap.put("message", "삭제된 게시글이에요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
=======
    if(detailInfo == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 게시글이에요.")));
    
    else if(detailInfo.getAiPublic() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 비공개된 게시글이에요.")));
     
    else if(detailInfo.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글이에요.")));
     
>>>>>>> rsj6
    else{
        response = ArticleDetailVO.builder()
        .aiSeq(detailInfo.getAiSeq())
        .aiTitle(detailInfo.getAiTitle())
        .content(detailInfo.getAiContent())
        .regDt(detailInfo.getAiRegDt())
        .aiModDt(detailInfo.getAiModDt())
        .aiStatus(detailInfo.getAiStatus())
        .aiPurpose(detailInfo.getAiPurpose())
        .aiPublic(detailInfo.getAiPublic())
        .biSeq(detailInfo.getAiBiSeq())
        .uiSeq(detailInfo.getAiUiSeq())
        .showImgInfo(showImgInfo)
        .showComment(showComment)
        .build();
<<<<<<< HEAD
        // resultMap.put("data", detailInfo);
        // resultMap.put("imgData", showImgInfo);
        // resultMap.put("commentData", showComment);
        // resultMap.put("status", true);
        // resultMap.put("message", aiSeq+"번 게시글의 상세보기 내용입니다.");
        // resultMap.put("code", HttpStatus.OK);
=======
>>>>>>> rsj6
    } 
    return response;
}

// 게시글 수정
<<<<<<< HEAD
public ArticleInfoEntity modifyArticle(ArticleModifyVO data){
    Map<String, Object> resultMap = new HashMap<>();
=======
public ArticleModifyResponse modifyArticle(ArticleModifyVO data){
>>>>>>> rsj6
    ArticleInfoEntity modifyPost = null;
    ArticleModifyResponse response = null;
    // 수정할 게시글을 선택
    modifyPost = articleInfoRepo.findByAiSeq(data.getAiSeq());
    // 수정일
    LocalDateTime modifyDate = LocalDateTime.now();
<<<<<<< HEAD
    if(modifyPost == null){
        resultMap.put("status", false);
        resultMap.put("message", "게시글이 존재하지 않습니다.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(modifyPost.getAiUiSeq() != data.getUiSeq().intValue()){
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
        if (data.getContent() != null) {
            modifyPost.setAiContent(data.getContent());
        }
        if (data.getAiPublic() != null) {
            modifyPost.setAiPublic(data.getAiPublic());
        }
        if (data.getFiles() != null){

        try{
            imgfileHandler(data.getFiles(), data.getAiSeq());
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
    return modifyPost;
=======
    if(modifyPost == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 게시글이 존재하지 않아요.")));
    
    else if(modifyPost.getAiUiSeq() != data.getUiSeq().intValue())
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 게시글은 수정할 수 없어요.")));
    
    else if(modifyPost.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글 입니다.")));

    else{
        if (data.getAiTitle() != null)     modifyPost.setAiTitle(data.getAiTitle());
        if (data.getContent() != null)     modifyPost.setAiContent(data.getContent());
        if (data.getAiPublic() != null)    modifyPost.setAiPublic(data.getAiPublic());
        if (data.getFiles() != null){
            try{
                imgfileHandler(data.getFiles(), data.getAiSeq());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        modifyPost.setAiModDt(modifyDate);
        articleInfoRepo.save(modifyPost);
        List<GetImgInfoVO> fileInfo = articleImgRepo.findByAimgAiSeq(modifyPost.getAiSeq());
        response = ArticleModifyResponse.builder()
        .aiSeq(modifyPost.getAiSeq())
        .aiTitle(modifyPost.getAiTitle())
        .content(modifyPost.getAiContent())
        .aiPublic(modifyPost.getAiPublic())
        .uiSeq(modifyPost.getAiUiSeq())
        .files(fileInfo)
        .build();

    }
    return response;
>>>>>>> rsj6
}

// 게시글 삭제
public ResponseMessageVO deleteArticle(Long uiSeq, Long aiSeq){
    ResponseMessageVO response = null;
    ArticleInfoEntity deletePost = null;
    deletePost = articleInfoRepo.findByAiSeq(aiSeq);
    if(Objects.isNull(deletePost))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 게시글이 존재하지 않아요.")));
    
    else if(deletePost.getAiUiSeq() != uiSeq.intValue())
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 게시글은 삭제할 수 없어요.")));

    else if(deletePost.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 게시글이에요.")));
    
    else{
        deletePost.setAiStatus(2);
        articleInfoRepo.save(deletePost);
        response = ResponseMessageVO.builder()
        .status(true)
        .message(" 게시글이 삭제되었습니다.")
        .build();
    }
    return response;
}

//댓글 작성
<<<<<<< HEAD
public ArticleCommentEntity postComment(PostWriterCommentVO data){
    Map<String, Object> resultMap = new HashMap<>();
    ArticleCommentEntity comment = null;
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
         comment = ArticleCommentEntity.builder()
                                        .acContent(data.getAcContent())
                                        .acAiSeq(data.getAcAiSeq())
                                        .acUiSeq(data.getAcUiSeq())
                                        .build();
                                        ArticleCommentRepo.save(comment);
        // resultMap.put("status", true);
        // resultMap.put("message", "댓글이 등록되었습니다..");
        // resultMap.put("code", HttpStatus.OK);
        
    }

    return comment;
}

// 댓글 수정
public Map<String, Object> patchComment(Long uiSeq, Long acSeq, PatchCommentVO data ){
    Map<String, Object> resultMap = new HashMap<>();
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    
    if(ObjectUtils.isEmpty(userInfo)){
        resultMap.put("status", false);
        resultMap.put("message", "로그인을 해주세요.");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else {
        ArticleCommentEntity commentInfo = ArticleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo)){
            resultMap.put("status", false);
            resultMap.put("message", "존재하지 않는 댓글이에요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(commentInfo.getAcUiSeq() != uiSeq){
            resultMap.put("status", false);
            resultMap.put("message", "다른사람의 댓글은 수정할 수 없어요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(commentInfo.getAcStatus() == 2){
            resultMap.put("status", false);
            resultMap.put("message", "삭제된 댓글이에요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(data.getAcContent().isEmpty()){
            resultMap.put("status", false);
            resultMap.put("message", "내용을 입력해주세요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else{
            LocalDateTime modDt = LocalDateTime.now();
            commentInfo.setAcContent(data.getAcContent());
=======
public CommentResponse postComment(Long acAiSeq, Long acUiSeq,PostWriterCommentVO data){
    ArticleCommentEntity comment = null;
    UserInfoEntity user = userInfoRepo.findByUiSeq(acUiSeq);
    ArticleInfoEntity article = articleInfoRepo.findByAiSeq(acAiSeq);
    CommentResponse response = null;
    
    if(data.getContent() == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("내용을 입력하세요.")));
    else if(article == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("존재하지 않는 게시글이에요.")));
    else if(article.getAiStatus() == 2)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("삭제된 게시글이에요.")));
    else if(user == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("로그인을 해주세요.")));
    else{
         comment = ArticleCommentEntity.builder()
                                        .acContent(data.getContent())
                                        .acAiSeq(acAiSeq)
                                        .acUiSeq(acUiSeq)
                                        .build();
                                        ArticleCommentRepo.save(comment);

        LocalDateTime regDt = LocalDateTime.now();
        response = CommentResponse.builder()
                                        .acSeq(comment.getAcSeq())
                                        .acContent(comment.getAcContent())
                                        .acRegDt(regDt)
                                        .acModDt(comment.getAcModDt())
                                        .acStatus(1)
                                        .acAiSeq(comment.getAcAiSeq())
                                        .acUiSeq(comment.getAcUiSeq())
                                        .build();
    }
    return response;
}

// 댓글 수정
public CommentResponse patchComment(Long uiSeq, Long acSeq, PatchCommentVO data ){
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    CommentResponse response = null;
    if(ObjectUtils.isEmpty(userInfo))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("로그인을 해주세요.")));
    
    else {
        ArticleCommentEntity commentInfo = ArticleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo))
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 댓글이에요.")));
        
        else if(commentInfo.getAcUiSeq() != uiSeq)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 댓글은 수정할 수 없어요.")));
        
        else if(commentInfo.getAcStatus() == 2)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 삭제된 댓글이에요.")));
        
        else if(data.getContent().isEmpty())
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("내용을 입력해주세요.")));
        
        else{
            LocalDateTime modDt = LocalDateTime.now().withNano(0);
            
            commentInfo.setAcContent(data.getContent());
>>>>>>> rsj6
            commentInfo.setAcModDt(modDt);
            commentInfo.setAcAiSeq(commentInfo.getAcAiSeq());
            commentInfo.setAcUiSeq(commentInfo.getAcUiSeq());
            ArticleCommentRepo.save(commentInfo);
<<<<<<< HEAD
        
            resultMap.put("status", true);
            resultMap.put("message", "댓글을 수정했어요.");
            resultMap.put("code", HttpStatus.OK);
        }
    }
    return resultMap;
}
        
        
    }
=======

            response = CommentResponse.builder()
                .acSeq(commentInfo.getAcSeq())
                .acContent(commentInfo.getAcContent())
                .acRegDt(commentInfo.getAcRegDt())
                .acModDt(commentInfo.getAcModDt())
                .acStatus(commentInfo.getAcStatus())
                .acAiSeq(commentInfo.getAcAiSeq())
                .acUiSeq(commentInfo.getAcUiSeq())
                .build();
        }
    }
    return response;
}
        
        
// 댓글 삭제
public ResponseMessageVO deleteComment(Long uiSeq, Long acSeq){
    ResponseMessageVO response = null;
    UserInfoEntity userInfo = userInfoRepo.findByUiSeq(uiSeq);
    
    if(ObjectUtils.isEmpty(userInfo))
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format("로그인을 해주세요.")));
    
    else {
        ArticleCommentEntity commentInfo = ArticleCommentRepo.findByAcSeq(acSeq);
        
        if(ObjectUtils.isEmpty(commentInfo))
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 존재하지 않는 댓글이에요.")));
        
        else if(commentInfo.getAcUiSeq() != uiSeq)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 다른사람의 댓글은 삭제할 수 없어요.")));
        
        else if(commentInfo.getAcStatus() == 2)
            throw new ReadersProjectException(ErrorResponse.of(HttpStatus.BAD_REQUEST, String.format(" 이미 삭제된 댓글이에요.")));
        
        else {
            commentInfo.setAcStatus(2);
            ArticleCommentRepo.save(commentInfo);
            response = ResponseMessageVO.builder()
            .message("댓글을 삭제했어요.")
            .status(true)
            .build();
        }
        }
    return response;
    }
}
>>>>>>> rsj6
    

