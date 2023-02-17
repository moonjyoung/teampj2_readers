package com.readers.be3.api;

import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;

import com.readers.be3.service.BookService;
import com.readers.be3.utilities.ResponseMessageUtils;
import com.readers.be3.vo.book.BookInfoImgVO;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.book.ResponseBookInfoVO;
import com.readers.be3.vo.response.BasicResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서 정보 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookAPIController {
    private final BookService bookService;
    @Value("${file.image.book}") String book_img_path;

    @Operation(summary = "책 정보 추가", description = "책을 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBookInfoVO> addBookInfo(
        @Parameter(description = "formdata로 데이터와 이미지 파일을 입력합니다.") @ModelAttribute BookInfoImgVO data
    ) {
        if(data.getImg().isEmpty()) {
            throw new MultipartException("이미지는 필수값입니다.");
        }
        return new ResponseEntity<>(bookService.addBookInfo(data), HttpStatus.CREATED);
    }

    @Operation(summary = "책 검색", description = "검색어가 제목 or 작가 or 출판사에 포함된 책을 검색합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class))),
            @ApiResponse(responseCode = "500", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @GetMapping("/search")
    public ResponseEntity< List<ResponseBookInfoVO> > searchBookInfo(
        @Parameter(description = "검색어", example = "생에") @RequestParam String keyword
    ) {
        if (keyword.trim()==null || keyword.trim()=="") {
            throw new NullPointerException("검색어가 없거나 공백입니다.");
        }
        else if (keyword.trim().length()==1) {
            throw new InvalidInputException("검색어는 2글자 이상이어야 합니다.");
        }
        return new ResponseEntity<>(bookService.searchBookInfo(keyword.trim()), HttpStatus.OK);
    }

    @Operation(summary = "책 이미지 다운로드", description = "가장 최근에 등록된 책 이미지를 다운받습니다.")
    @GetMapping("/download/{uri}")
    public ResponseEntity<Resource> getImgage(
        @Parameter(description = "다운받을 파일 명(책 제목)을 입력합니다.", example = "생에 감사해") @PathVariable String uri, 
        HttpServletRequest request
    ) throws Exception {
        Path folderLocation = Paths.get(book_img_path);
        String filename = bookService.getFilenameByUri(uri);

        // 폴더 경로 + 파일 이름 -> targetFile 경로 생성
        String[] split = filename.split("\\.");
        String ext = split[split.length - 1];
        String exportName = uri + "." + ext;
        Path targetFile = folderLocation.resolve(filename);

        // 다운로드 가능한 형태로 변환하기 위한 Resource객체 생성
        Resource r = null;
        try {
            r = new UrlResource(targetFile.toUri());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 첨부된 파일 타입(jpg 등) 저장하기 위한 변수 생성
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            if (contentType==null) contentType = "application/octet-stream";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=\"" + URLEncoder.encode(exportName, "UTF-8") + "\"")
            .body(r);
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
