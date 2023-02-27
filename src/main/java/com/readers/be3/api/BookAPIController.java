package com.readers.be3.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.BookService;
import com.readers.be3.service.ScheduleService;
import com.readers.be3.utilities.ResponseMessageUtils;
import com.readers.be3.vo.book.BookInfoAladinVO;
import com.readers.be3.vo.book.GetMyBookVO;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.book.PatchBookStatusVO;
import com.readers.be3.vo.book.ResponseBookInfoVO;
import com.readers.be3.vo.response.BasicResponse;
import com.readers.be3.vo.schedule.AddScheduleVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서 정보 관리", description = "회원별 도서 정보 추가/조회/관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mybook")
public class BookAPIController {
    private final BookService bookService;
    private final ScheduleService scheduleService;
    // @Value("${file.image.book}") String book_img_path;

    @Operation(summary = "책 정보 추가", description = "aladin 검색 api를 통해 책 정보를 받아와서 추가합니다. DB에 없는 책은 DB에 저장후 리턴, 있는 책은 저장값을 리턴합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @PostMapping(value = "/add/{type}")
    public ResponseEntity<ResponseBookInfoVO> addBookInfo(
        @Parameter(description = "완독여부 정보를 받습니다.(plan : 읽을예정, read : 완독)", example = "plan") @PathVariable String type,
        @Parameter(description = "회원 번호", example = "110") @RequestParam Long uiSeq,
        @Parameter(description = "알라딘 검색 api를 통해 데이터를 받아옵니다. DB에 없는 책은 DB에 저장후 리턴, 있는 책은 저장값을 리턴합니다.") @RequestBody BookInfoAladinVO data
    ) {
        ResponseBookInfoVO vo = bookService.addBookInfo(data);
        if      (type.equals("plan")) vo.setStatus(1);
        else if (type.equals("read")) vo.setStatus(4);
        else    throw new InvalidInputException("유효하지 않은 status type입니다.");
        vo.setUiSeq(uiSeq);

        AddScheduleVO sVO = new AddScheduleVO(vo);
        scheduleService.addSchedule(sVO);
        return new ResponseEntity<>(vo, HttpStatus.CREATED);
    }

    @Operation(summary = "책 정보 조회", description = "회원이 추가한 책 목록을 조건에 따라 출력합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @GetMapping(value = "/list/{type}")
    public ResponseEntity<GetMyBookVO> getBookList(
        @Parameter(description = "완독여부 정보를 받습니다.(all : 전체, plan : 읽을예정&계획, read : 완독)", example = "all") @PathVariable String type,
        @Parameter(description = "회원 번호", example = "110") @RequestParam Long uiSeq
    ) {
        Integer status = -1;
        if (type.equals("all")) status = 0;
        else if (type.equals("read")) status = 4;
        else if (type.equals("plan")) status = 9;
        else throw new InvalidInputException("유효하지 않은 type입니다.");
        return new ResponseEntity<>(bookService.getMyBookList(uiSeq, status), HttpStatus.OK);
    }

    @Operation(summary = "책 상태 변경", description = "책 상태(완독여부)를 변경합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @PostMapping(value = "/status")
    public ResponseEntity<BasicResponse> patchBookStatus(@RequestBody PatchBookStatusVO data) {
        if (data.getId()==null || data.getStatus()==null) throw new InvalidInputException("데이터에 null값이 있습니다.");
        return new ResponseEntity<>(bookService.patchBookStatus(data), HttpStatus.CREATED);
    }

    // @Operation(summary = "책 정보 추가", description = "책을 추가합니다.",
    //     responses = {
    //         @ApiResponse(responseCode = "201", description = ResponseMessageUtils.TRUE),
    //         @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
    //             content = @Content(schema = @Schema(implementation = BasicResponse.class)))
    //     })
    // @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<ResponseBookInfoVO> addBookInfo(
    //     @Parameter(description = "formdata로 데이터와 이미지 파일을 입력합니다.") @ModelAttribute BookInfoImgVO data
    // ) {
    //     if(data.getImg().isEmpty()) {
    //         throw new MultipartException("이미지는 필수값입니다.");
    //     }
    //     return new ResponseEntity<>(bookService.addBookInfo(data), HttpStatus.CREATED);
    // }

    // @Operation(summary = "책 검색", description = "검색어가 제목 or 작가 or 출판사에 포함된 책을 검색합니다.",
    //     responses = {
    //         @ApiResponse(responseCode = "200", description = ResponseMessageUtils.TRUE),
    //         @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
    //             content = @Content(schema = @Schema(implementation = BasicResponse.class)))
    //     })
    // @GetMapping("/search")
    // public ResponseEntity< List<ResponseBookInfoVO> > searchBookInfo(
    //     @Parameter(description = "검색어", example = "생에") @RequestParam String keyword,
    //     @Parameter(description = "정렬 기준(1 or null:기본, 2:책제목)", example = "") @RequestParam @Nullable Integer sort
    // ) {
    //     if (keyword.trim()==null || keyword.trim()=="") {
    //         throw new NullPointerException("검색어가 없거나 공백입니다.");
    //     }
    //     else if (keyword.trim().length()==1) {
    //         throw new InvalidInputException("검색어는 2글자 이상이어야 합니다.");
    //     }
    //     return new ResponseEntity<>(bookService.searchBookInfo(keyword.trim(), sort), HttpStatus.OK);
    // }

    // @Operation(summary = "책 이미지 다운로드", description = "가장 최근에 등록된 책 이미지를 다운받습니다.")
    // @GetMapping("/download/{uri}")
    // public ResponseEntity<Resource> getImgage(
    //     @Parameter(description = "다운받을 파일 명(책 제목)을 입력합니다.", example = "생에 감사해") @PathVariable String uri, 
    //     HttpServletRequest request
    // ) throws Exception {
    //     Path folderLocation = Paths.get(book_img_path);
    //     String filename = bookService.getFilenameByUri(uri);

    //     // 폴더 경로 + 파일 이름 -> targetFile 경로 생성
    //     String[] split = filename.split("\\.");
    //     String ext = split[split.length - 1];
    //     String exportName = uri + "." + ext;
    //     Path targetFile = folderLocation.resolve(filename);

    //     // 다운로드 가능한 형태로 변환하기 위한 Resource객체 생성
    //     Resource r = null;
    //     try {
    //         r = new UrlResource(targetFile.toUri());
    //     }
    //     catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     // 첨부된 파일 타입(jpg 등) 저장하기 위한 변수 생성
    //     String contentType = null;
    //     try {
    //         contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
    //         if (contentType==null) contentType = "application/octet-stream";
    //     }
    //     catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return ResponseEntity.ok()
    //         .contentType(MediaType.parseMediaType(contentType))
    //         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=\"" + URLEncoder.encode(exportName, "UTF-8") + "\"")
    //         .body(r);
    // }

    // 이미지 파일 업로드 테스트 코드
    // @Operation(summary = "책 이미지 추가", description = "책 이미지 추가 테스트")
    // @PostMapping("/img/add")
    // public ResponseEntity<Object> addBookImg(
    //     @Parameter(description = "multipartfile로 이미지 파일 업로드") @RequestPart MultipartFile file
    // ) {
    //     return new ResponseEntity<Object>(bookService.addBookImg(file), HttpStatus.OK);
    // }
}
