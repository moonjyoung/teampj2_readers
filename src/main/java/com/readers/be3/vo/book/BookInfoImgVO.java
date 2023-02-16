package com.readers.be3.vo.book;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookInfoImgVO {
    @Schema(description = "책 고유번호(입력시엔 null로 입력)")
    private Long biSeq;
    @Schema(description = "제목", example = "생에 감사해")
    private String biName;
    @Schema(description = "저자", example = "김혜자")
    private String biAuthor;
    @Schema(description = "출판사", example = "수오서재")
    private String biPublisher;
    @Schema(description = "총 페이지 수", example = "376")
    private Integer biPage;
    @Schema(description = "ISBN", example = "9791190382915")
    private String biIsbn;
    @Schema(description = "책 표지", example = "생에 감사해.jpg", required = true)
    private MultipartFile img;
}
