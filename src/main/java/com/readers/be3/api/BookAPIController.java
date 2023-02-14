package com.readers.be3.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.BookService;
import com.readers.be3.vo.book.BookInfoImgVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서 정보 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookAPIController {
    private final BookService bookService;

    @Operation(summary = "책 정보 추가", description = "책을 추가합니다.")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addBookInfo(
        @Parameter(description = "formdata로 데이터와 이미지 파일을 입력합니다.") @ModelAttribute BookInfoImgVO data
    ) {
        return new ResponseEntity<>(bookService.addBookInfo(data), HttpStatus.OK);
    }

    @Operation(summary = "책 검색", description = "제목에 검색어가 포함된 책을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<Object> searchBookInfo(
        @Parameter(description = "검색어", example = "생에") @RequestParam String keyword
    ) {
        return new ResponseEntity<>(bookService.searchBookInfo(keyword), HttpStatus.OK);
    }

    // 이미지 파일 업로드 테스트 코드
    // @Operation(summary = "책 이미지 추가", description = "책 이미지 추가 테스트")
    // @PostMapping("/img/add")
    // public ResponseEntity<Object> addBookImg(
    //     @Parameter(description = "multipartfile로 이미지 파일 업로드") @RequestPart MultipartFile file
    // ) {
    //     return new ResponseEntity<Object>(bookService.addBookImg(file), HttpStatus.OK);
    // }
}
