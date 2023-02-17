package com.readers.be3.vo.mypage;

import java.util.List;

import com.readers.be3.entity.FinishedBookView;
import com.readers.be3.entity.MyPageView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseFinishedBookVO {
    // private List<MyPageView> list;

    @Schema(description = "책제목" , example = "달과 6펜스")
    private String bookTitle;
    @Schema(description = "작가이름" , example = "서머셋 몸")
    private String author;
    @Schema(description = "출판사명" , example = "민음사")
    private String publisher;

    public static ResponseFinishedBookVO toResponse(FinishedBookView fBookView) {
        return new ResponseFinishedBookVO(fBookView.getBiName(),
        fBookView.getBiAuthor(),
        fBookView.getBiPublisher());
    }
}
