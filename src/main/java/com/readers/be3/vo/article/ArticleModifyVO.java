package com.readers.be3.vo.article;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

// 게시글 수정할 데이터
@Data
public class ArticleModifyVO {
    
private String aiTitle;
private String aiContent;
private LocalDateTime aiModDt;
private Integer aiPublic;
}
