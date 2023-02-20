package com.readers.be3.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Immutable
@Table (name = "search_article_view")
public class SearchArticleView {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_seq") private Long aiSeq;
    @Column(name = "ai_title") private String aiTitle;
    @Column(name = "ai_status") private Integer aiStatus;
    @Column(name = "ai_purpose") private Integer aiPurpose;
    @Column(name = "ai_public") private Integer aiPublic;
    @Column(name = "ai_bi_seq") private Integer aiBiSeq;
    @Column(name = "ai_ui_seq") private Integer aiUiSeq;
    @Column(name = "ui_nickname") private String uiNickname;
    
}
