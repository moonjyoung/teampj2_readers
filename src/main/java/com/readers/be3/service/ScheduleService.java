package com.readers.be3.service;

import org.springframework.stereotype.Service;

import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.vo.schedule.AddScheduleVO;
import com.readers.be3.vo.schedule.ViewScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;

    public ViewScheduleVO addSchedule(AddScheduleVO data) {

        ViewScheduleVO vo = new ViewScheduleVO();
        if (bookInfoRepository.findById(data.getBiSeq()).isEmpty()) {
            return vo;
        }
        vo.setBookTitle(bookInfoRepository.findById(data.getBiSeq()).get().getBiName());
        vo.setDescription(data.getDescription());
        vo.setStartDate(data.getStartDate());
        vo.setEndDate(data.getEndDate());
        vo.setStatus(data.getStatus());

        ScheduleInfoEntity entity = ScheduleInfoEntity.builder()
                .siContent(data.getDescription())
                .siStartDate(data.getStartDate())
                .siEndDate(data.getEndDate())
                .siStatus(data.getStatus())
                .siUiSeq(data.getUiSeq())
                .siBiSeq(data.getBiSeq()).build();

        scheduleInfoRepository.save(entity);
        vo.setSiSeq(entity.getSiSeq());
        return vo;
    }

    public void deleteSchedule(Long siSeq) {
        ScheduleInfoEntity entity = scheduleInfoRepository.findById(siSeq).get();
        scheduleInfoRepository.delete(entity);
    }
}
