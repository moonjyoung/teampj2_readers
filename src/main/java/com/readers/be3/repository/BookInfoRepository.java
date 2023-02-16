package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readers.be3.entity.BookInfoEntity;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long>{

    BookInfoEntity findByBiSeq(Long biSeq);
    public List<BookInfoEntity> findByBiNameContains(String biName);
    public List<BookInfoEntity> findByBiNameContainsOrBiAuthorContainsOrBiPublisherContains(String name, String author, String publisher);
}
