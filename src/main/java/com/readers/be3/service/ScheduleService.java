package com.readers.be3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.schedule.AddScheduleVO;
import com.readers.be3.vo.schedule.UpdateScheduleVO;
import com.readers.be3.vo.schedule.ViewScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;

    public List<ViewScheduleVO> getSchedule(Long uiSeq) {
        List<ViewScheduleVO> list = new ArrayList<ViewScheduleVO>();
        for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeq(uiSeq)) {
            ViewScheduleVO vo = new ViewScheduleVO(entity);
            list.add(vo);
        }
        return list;
    }

    public ViewScheduleVO addSchedule(AddScheduleVO data) {
        ViewScheduleVO vo = new ViewScheduleVO();
        Integer status = 1;
        if (bookInfoRepository.findById(data.getBiSeq()).isEmpty()) {
            throw new InvalidInputException("존재하지 않는 책 번호 입니다.");
        }
        if (data.getStartDate()!=null) {
            status = 2;
            if (data.getEndDate()!=null) {
                status = 4;
            }
        }
        vo.setBookTitle(bookInfoRepository.findById(data.getBiSeq()).get().getBiName());
        vo.setDescription(data.getDescription());
        vo.setStartDate(data.getStartDate());
        vo.setEndDate(data.getEndDate());
        vo.setStatus(status);

        ScheduleInfoEntity entity = ScheduleInfoEntity.builder()
                .siContent(data.getDescription())
                .siStartDate(data.getStartDate())
                .siEndDate(data.getEndDate())
                .siStatus(status)
                .siUiSeq(data.getUiSeq())
                .siBiSeq(data.getBiSeq()).build();

        scheduleInfoRepository.save(entity);
        vo.setSiSeq(entity.getSiSeq());
        return vo;
    }

    public void deleteSchedule(Long siSeq) {
        ScheduleInfoEntity entity = scheduleInfoRepository.findById(siSeq)
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 스케쥴입니다."));
        scheduleInfoRepository.delete(entity);
    }

    public ViewScheduleVO updateSchedule(UpdateScheduleVO data) {
        ScheduleInfoEntity entity = scheduleInfoRepository.findById(data.getSiSeq())
                .orElseThrow(() -> new InvalidInputException("존재하지 않는 스케쥴입니다."));
        
        Long uiSeq = entity.getSiUiSeq();
        Long biSeq = entity.getSiBiSeq();
        Integer status = 1;
        if (data.getSiStartDate()!=null) {
            status = 2;
            if (data.getSiEndDate()!=null) {
                status = 4;
            }
        }

        ScheduleInfoEntity newEntity = ScheduleInfoEntity.builder()
                .siBiSeq(data.getSiSeq())
                .siContent(data.getSiContent())
                .siStartDate(data.getSiStartDate())
                .siEndDate(data.getSiEndDate())
                .siUiSeq(uiSeq).siBiSeq(biSeq)
                .siStatus(status).build();
        scheduleInfoRepository.save(newEntity);

        return new ViewScheduleVO(newEntity);
    }
}
