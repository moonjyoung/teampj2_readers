package com.readers.be3.service;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.readers.be3.vo.article.articleImgVO;
import com.readers.be3.vo.article.writeArticleVO;

@Service
public class ArticleService {
    @Autowired ArticleImgRepository articleImgRepo;
    @Autowired ArticleInfoRepository articleInfoRepo;
    @Autowired UserInfoRepository userInfoRepo;
    
    // 독후감 작성 기능
    public Map<String, Object> writeArticle(writeArticleVO data, List<MultipartFile> files){
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
        String path = "images/" + current_date;
        // path 경로를 갖는 file 객체를 생성
        File file = new File(path);
        
        // 저장할 위치의 디렉토리가 존재하지 않을 경우
        if(!file.exists()){
            file.mkdirs();
        }

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
// pathvarible 로 검색타입(작성자, 제목, 내용 )
// type => (writer, title, content)

public Map<String, Object> getArticleList(String type, String keyword, Pageable pageable){
Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
if(type == null){
    Page<ArticleInfoEntity> page = articleInfoRepo.findAll(pageable);

    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "전체 게시글 리스트 조회.");
    resultMap.put("code", HttpStatus.OK);
}
else if(type == "writer"){
    // 유저 아이디 검색해서 해당하는 유저정보 가져오기
    UserInfoEntity writerInfo = userInfoRepo.findByUiEmail(keyword);
    // 검색한 유저정보에서 유저seq로 작성한 게시글 검색
    Page<ArticleInfoEntity> page = articleInfoRepo.findByAiUiSeq(writerInfo.getUiSeq(), pageable);

    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "작성자로 검색.");
    resultMap.put("code", HttpStatus.OK);
}
else if(type == "title"){
    Page<ArticleInfoEntity> page = articleInfoRepo.findByAiTitleContains(keyword, pageable);
    
    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "제목을 검색.");
    resultMap.put("code", HttpStatus.OK);
}
else if(type == "content"){
    Page<ArticleInfoEntity> page = articleInfoRepo.findByAiContentContains(keyword, pageable);

    resultMap.put("data", page);
    resultMap.put("status", true);
    resultMap.put("message", "내용으로 검색.");
    resultMap.put("code", HttpStatus.OK);
}
else{
    resultMap.put("status", false);
    resultMap.put("message", "잘못된 검색 타입이에요 (type = (writer, title, content)).");
    resultMap.put("code", HttpStatus.BAD_REQUEST);
}
return resultMap;
}
}
