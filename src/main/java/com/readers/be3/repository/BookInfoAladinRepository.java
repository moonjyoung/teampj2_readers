package com.readers.be3.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.BookInfoAladinEntity;

public interface BookInfoAladinRepository extends JpaRepository<BookInfoAladinEntity, Long> {
    public List<BookInfoAladinEntity> findByBiNameContainsOrBiAuthorContainsOrBiPublisherContains(String name, String author, String publisher, Sort sort);
}
