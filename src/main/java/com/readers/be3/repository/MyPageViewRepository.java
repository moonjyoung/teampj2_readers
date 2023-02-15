package com.readers.be3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.readers.be3.entity.MyPageView;
import com.readers.be3.entity.UserRankView;

public interface MyPageViewRepository extends JpaRepository<MyPageView,Long>{

  MyPageView findByUiSeq(Long uiSeq);

  
}
