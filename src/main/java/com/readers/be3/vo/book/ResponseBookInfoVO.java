package com.readers.be3.vo.book;

import com.readers.be3.entity.BookInfoAladinEntity;
import com.readers.be3.entity.BookInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseBookInfoVO {
    @Schema(description = "책 고유번호", example = "1")
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
    @Schema(description = "책 이미지 URI", example = "생에 감사해.jpg")
    private String bimgUri;

    public ResponseBookInfoVO(BookInfoImgVO data) {
        this.biName = data.getBiName();
        this.biAuthor = data.getBiAuthor();
        this.biPublisher = data.getBiPublisher();
        this.biPage = data.getBiPage();
        this.biIsbn = data.getBiIsbn();
    }
    public ResponseBookInfoVO(BookInfoEntity data) {
        this.biSeq = data.getBiSeq();
        this.biName = data.getBiName();
        this.biAuthor = data.getBiAuthor();
        this.biPublisher = data.getBiPublisher();
        this.biPage = data.getBiPage();
        this.biIsbn = data.getBiIsbn();
    }
    public ResponseBookInfoVO(BookInfoAladinEntity data) {
        this.biSeq = data.getBiSeq();
        this.biName = data.getBiName();
        this.biAuthor = data.getBiAuthor();
        this.biPublisher = data.getBiPublisher();
        this.biPage = data.getBiPage();
        this.biIsbn = data.getBiIsbn();
        this.bimgUri = data.getBiUri();
    }
    public ResponseBookInfoVO(BookInfoAladinVO data) {
        this.biName = data.getBiName();
        this.biAuthor = data.getBiAuthor();
        this.biPublisher = data.getBiPublisher();
        this.biPage = data.getBiPage();
        this.biIsbn = data.getBiIsbn();
        this.bimgUri = data.getBimgUri();
    }
    public ResponseBookInfoVO() {}
}
