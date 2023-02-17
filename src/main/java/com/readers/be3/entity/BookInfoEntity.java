package com.readers.be3.entity;

import org.hibernate.annotations.DynamicInsert;

import com.readers.be3.entity.image.BookImgEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "book_info")
public class BookInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bi_seq") private Long biSeq;
    @Column(name = "bi_name") private String biName;
    @Column(name = "bi_author") private String biAuthor;
    @Column(name = "bi_publisher") private String biPublisher;
    @Column(name = "bi_page") private Integer biPage;
    @Column(name = "bi_isbn") private String biIsbn;
    @OneToOne(mappedBy = "bookInfoEntity") private BookImgEntity bookImgEntity;
}
