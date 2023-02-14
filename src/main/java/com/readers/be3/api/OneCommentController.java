package com.readers.be3.api;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import com.readers.be3.dto.request.OneCommentDeleteRequest;
import com.readers.be3.dto.request.OneCommentRequest;
import com.readers.be3.service.OneCommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "한줄평", description = "한줄평 crd api")
@RestController
@RequestMapping("/api/onecomment")
public class OneCommentController {

  @Autowired OneCommentService oneCommentService;

  @Operation(summary = "한줄평 추가", description = "한줄평을 추가합니다")
  @PostMapping("/add")
  public Map<String,Object> OneCommentAdd(@Parameter(description = "한줄평추가 dto") @RequestBody OneCommentRequest request){
    return oneCommentService.OneCommentAdd(request.getUserSeq(),request.getBookSeq(),request.getComment(),request.getScore());
  }

  @Operation(summary = "한줄평 삭제", description = "등록된 한줄평 delete를 삭제처리합니다")
  @DeleteMapping("/delete")
  public Map<String,Object> OneCommentAdd(@Parameter(description = "삭제dto") @RequestBody OneCommentDeleteRequest request){
    return oneCommentService.OneCommentDelete(request.getUserSeq(), request.getOneCommentSeq());
  }

  @Operation(summary = "한줄평 리스트", description = "등록된 한줄평을 10개단위로 책번호를통해 조회합니다")
  @GetMapping("/{bookseq}/list")
  public Map<String,Object> OneCommentList(@Parameter(description = "책번호", example = "1") @PathVariable("bookseq") Long bookseq,
  @Parameter(description = "페이지", example = "0") @RequestParam Integer page){
    Sort sort2 = Sort.by("ocSeq").ascending();
    Pageable pageable = PageRequest.of(page, 10, sort2);
    return oneCommentService.oneCommentList(bookseq, pageable);
  }
}
