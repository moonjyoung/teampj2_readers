package com.readers.be3.vo.article;

import lombok.Builder;
import lombok.Data;

// 게시글 작성할때 받을 데이터( 제목과 내용 )
@Builder
@Data
public class writeArticleVO {
    private String aiTitle;
    private String aiContent;
    private Integer aiPublic;
}
