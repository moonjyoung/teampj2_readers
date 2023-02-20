package com.readers.be3.vo.article;

import java.time.LocalDateTime;

import lombok.Data;

// 게시글 목록 조회
public interface GetSearchArticleVO {
    public Long getAiSeq();
    public String getAiTitle();
    public LocalDateTime getAiRegDt();
    public LocalDateTime getAiModDt();
    public Integer getAiStatus();
    public Integer getAiPurpose();
    public Integer getAiPublic();
    public Integer getAiUiSeq();
    public Integer getAiBiSeq();
}
