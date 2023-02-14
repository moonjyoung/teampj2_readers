package com.readers.be3.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "one_comment")
public class OneCommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oc_seq") private Long ocSeq;
    @Column(name = "oc_comment") private String ocComment;
    @Column(name = "oc_score") private Integer ocScore;
    @Column(name = "oc_status")  private Integer ocStatus;
    @Column(name = "oc_reg_dt") private LocalDateTime ocRegDt;
    @JoinColumn(name = "oc_ui_seq") @OneToOne(fetch = FetchType.EAGER)  private UserInfoEntity userInfoEntity;
    @JoinColumn(name = "oc_bi_seq") @OneToOne(fetch = FetchType.LAZY) @JsonIgnore private BookInfoEntity bookInfoEntity;

    public static OneCommentEntity of(String comment, Integer score, UserInfoEntity userInfoEntity,BookInfoEntity bookInfoEntity){
        OneCommentEntity oneCommentEntity = new OneCommentEntity();
        oneCommentEntity.setOcComment(comment);
        oneCommentEntity.setOcScore(score);
        oneCommentEntity.setUserInfoEntity(userInfoEntity);
        oneCommentEntity.setBookInfoEntity(bookInfoEntity);
        oneCommentEntity.setOcStatus(1);
        oneCommentEntity.setOcRegDt(LocalDateTime.now());
        return oneCommentEntity;
    }
}
