package com.readers.be3.vo.article;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

// 게시글 작성할때 받을 데이터( 제목과 내용 )
@Builder
@Data
public class WriteArticleVO {
    private String aiTitle;
    private String aiContent;
    private Integer aiPublic;
    private Integer uiSeq;
    private Integer biSeq;
}
