package com.readers.be3.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "article_comment")
public class ArticleCommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ac_seq") private Long acSeq;
    @Column(name = "ac_content") private String acContent;
    @Column(name = "ac_reg_dt") private LocalDateTime acRegDt;
    @Column(name = "ac_mod_dt") private LocalDateTime acModDt;
    @Column(name = "ac_status") private Integer acStatus;
    @Column(name = "ac_ai_seq") private Long acAiSeq;
    @Column(name = "ac_ui_seq") private Long acUiSeq;
}
