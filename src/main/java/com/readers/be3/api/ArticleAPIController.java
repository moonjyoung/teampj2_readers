package com.readers.be3.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.service.ArticleService;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.WriteArticleVO;

import io.micrometer.common.lang.Nullable;

@RestController
@RequestMapping("/api")
public class ArticleAPIController {
    @Autowired ArticleService articleService;

    // 게시글 등록 api (이미지 파일 저장이 안됨)
    @PostMapping("/article")
    public ResponseEntity<Object> writeArticle(@ModelAttribute WriteArticleVO data, List<MultipartFile> files){
        Map <String, Object> resultMap = articleService.writeArticle(data, files);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    // 게시글 조회 api
    @GetMapping("/article/{type}")
    public ResponseEntity<Object> searchArticle(@PathVariable String type, @RequestParam(required = false, value = "keyword") String keyword, 
    Pageable pageable){
        Map<String, Object> resultMap = articleService.getArticleList(type, keyword, pageable);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    // 게시글 수정api (작업중)
    // @PatchMapping("/article")
    // public ResponseEntity<Object> modifyArticle(@RequestParam Long uiSeq, @RequestBody ArticleModifyVO data){
    //     Map<String, Object> resultMap = articleService.modifyArticle(uiSeq, data);
    //     return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    // }
    
    

}
