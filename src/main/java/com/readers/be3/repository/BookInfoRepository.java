package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.readers.be3.entity.BookInfoEntity;

public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long>{

    BookInfoEntity findByBiSeq(Long biSeq);
    
    @Query(value = "select * from book_info where bi_name like '%:keyword%' or bi_author like '%:keyword%' or bi_publisher like '%:keyword%'", nativeQuery = true)
    public List<BookInfoEntity> findAllByKeywordContains(@Param("keyword") String keyword);
    public List<BookInfoEntity> findAllByBiNameContains(String keyword);
}
