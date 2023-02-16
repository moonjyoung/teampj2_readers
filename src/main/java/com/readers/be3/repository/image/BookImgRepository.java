package com.readers.be3.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readers.be3.entity.image.BookImgEntity;

public interface BookImgRepository extends JpaRepository<BookImgEntity, Long>{
    @Query(value = "select * from book_img where bimg_bi_seq = :bimgBiSeq", nativeQuery = true)
    public BookImgEntity findByBimgBiSeq(@Param("bimgBiSeq") Long bimgBiSeq);
    public BookImgEntity findTopByBimgUri(String bimgUri);
}
