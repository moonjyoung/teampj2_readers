package com.readers.be3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readers.be3.entity.ScheduleInfoEntity;

public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfoEntity, Long> {

  ScheduleInfoEntity findBySiSeq(Long SiSeq);
    
}
