package com.readers.be3.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.service.ArticleService;
import com.readers.be3.vo.article.writeArticleVO;

@RestController
// @RequestMapping("/article")
public class ArticleAPIController {
    @Autowired ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<Object> writeArticle(@ModelAttribute writeArticleVO data, List<MultipartFile> files, @RequestParam Long uiSeq){
        Map <String, Object> resultMap = articleService.writeArticle(data, files, uiSeq);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }
    

}
