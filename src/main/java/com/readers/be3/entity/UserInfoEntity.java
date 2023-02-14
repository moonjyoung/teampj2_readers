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
@Table(name = "user_info")
public class UserInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ui_seq") private Long uiSeq;
    @Column(name = "ui_email") private String uiEmail;
    @Column(name = "ui_pwd") private String uiPwd;
    @Column(name = "ui_nickname") private String uiNickname;
    @Column(name = "ui_reg_dt") private LocalDateTime uiRegDt;
    @Column(name = "ui_point") private Integer uiPoint;
}
