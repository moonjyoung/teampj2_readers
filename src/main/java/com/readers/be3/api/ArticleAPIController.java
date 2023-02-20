package com.readers.be3.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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
import org.springframework.http.MediaType;

import com.readers.be3.service.ArticleService;
import com.readers.be3.vo.article.ArticleModifyVO;
import com.readers.be3.vo.article.PostArticleVO;
import com.readers.be3.vo.article.PostWriterCommentVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시글 및 댓글")
@RestController
@RequestMapping("/api")
public class ArticleAPIController {
    @Autowired ArticleService articleService;

    // 게시글 등록 api
    @Operation(summary = "게시글 등록 api", description = "등록할 게시글 내용을 form-data로 받습니다.")
    @PostMapping(value = "/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> writeArticle(@ModelAttribute PostArticleVO data){
        Map <String, Object> resultMap = articleService.writeArticle(data);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    // 게시글 목록 조회및 검색 api
    @Operation(summary = "게시글 목록 조회 api", description = "게시글 리스트와 페이지 정보를 보여줍니다. type으로 검색 타입을 지정하고 keyword로 검색할 수 있습니다. *type=all     (전체게시글 조회)일 경우 keyword는 필요하지 않습니다.")
    @GetMapping("/article/{type}")
    public ResponseEntity<Object> searchArticle(
    @Parameter(description = "검색타입 all(전체), writer(작성자), title(제목), content(내용)", example = "all") @PathVariable String type,
    @Parameter(description = "검색어(type=all일 경우 사용하지 않습니다.)") @RequestParam(required = false, value = "keyword") String keyword,
    @Parameter(description = "페이지 정보 page(현재 페이지), size(한 페이지에 노출할 데이터 수), sort(정렬 조건(컬럼명,desc|asc)) *설정하지 않으면 기본값으로 설정됨") 
    Pageable pageable ){
        Map<String, Object> resultMap = articleService.getArticleList(type, keyword, pageable);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    // 게시글 상세조회 api
    @Operation(summary = "게시글 상세 조회", description = "게시글 번호(aiSeq)에 해당하는 게시글을 상세조회합니다.")
    @GetMapping("/article/detail")
    public ResponseEntity<Object> detailArticle(
        @Parameter(description = "검색할 게시글 번호", example = "1") @RequestParam Long aiSeq){
            Map<String, Object> resultMap = articleService.getArticleDetailInfo(aiSeq);
            return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }
    
    // 게시글 수정api (이미지 수정 작업중)
    @Operation(summary = "게시글 수정 api", description = "등록된 게시글의 제목과 내용, 이미지, 공개,비공개 여부를 수정합니다.")
    @PatchMapping("/article")
    public ResponseEntity<Object> modifyArticle(@RequestParam Long uiSeq, @RequestParam Long aiSeq, @RequestBody ArticleModifyVO data){
        Map<String, Object> resultMap = articleService.modifyArticle(uiSeq, aiSeq, data);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }


    // 게시글 삭제
    @Operation(summary = "게시글 삭제 api", description = "게시글을 삭제합니다(ai_public => 2로 변경).")
    @PatchMapping("/article/delete")
    public ResponseEntity<Object> deleteArticle(
        @Parameter(description = "현재 로그인한 사용자 번호") @RequestParam Long uiSeq,
        @Parameter(description = "삭제할 게시글 번호") @RequestParam Long aiSeq){
        Map<String, Object> resultMap = articleService.deleteArticle(uiSeq, aiSeq);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    // 댓글 작성
    @Operation(summary = "댓글 작성 api", description = "댓글작성")
    @PostMapping("/article/comment")
    public ResponseEntity<Object> writeComment(
    @RequestBody PostWriterCommentVO data)
    {
    Map<String, Object> resultMap = articleService.postWriteComment(data);
    return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }
}
