package com.readers.be3.entity.image;

import org.hibernate.annotations.DynamicInsert;

import com.readers.be3.entity.BookInfoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "book_img")
public class BookImgEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bimg_seq") private Long bimgSeq;
    @Column(name = "bimg_filename") private String bimgFilename;
    @Column(name = "bimg_uri") private String bimgUri;
    @Column(name = "bimg_bi_seq") private Long bimgBiSeq;
    @OneToOne @JoinColumn(name = "bimg_bi_seq", insertable = false, updatable = false) private BookInfoEntity bookInfoEntity;
}
