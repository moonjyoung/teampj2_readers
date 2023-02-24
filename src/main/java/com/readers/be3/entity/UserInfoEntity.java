package com.readers.be3.entity;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;

import com.readers.be3.entity.image.UserImgEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
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
    @Column(name = "ui_reg_dt") private Date uiRegDt;
    @Column(name = "ui_point") private Integer uiPoint;
    @Column(name = "ui_total_page") private Integer uiTotalPage;
    @Column(name = "ui_total_book") private Integer uiTotalBook;
    @Column(name = "ui_uid") private String uiUid;
    @Column(name = "ui_login_type") private String uiLoginType;
    @Column(name = "ui_status") private Integer uiStatus;
    @Column(name = "ui_leave_dt") private Date uiLeaveDt;
    @OneToOne(mappedBy = "userInfoEntity") private UserImgEntity userImgEntity;

    public static UserInfoEntity ofSNS(String uiUid, String uiLoginType){
        return new UserInfoEntity(null, null, null, "user#"+Calendar.getInstance().getTimeInMillis(), null, null, null, null, uiUid, uiLoginType, null, null, null);
    }
}
