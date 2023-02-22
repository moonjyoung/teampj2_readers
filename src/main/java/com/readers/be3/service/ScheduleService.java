package com.readers.be3.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.schedule.AddScheduleVO;
import com.readers.be3.vo.schedule.UpdateScheduleVO;
import com.readers.be3.vo.schedule.ViewScheduleVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserInfoRepository userInfoRepository;
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;

    public List<ViewScheduleVO> getSchedule(Long uiSeq) {
        List<ViewScheduleVO> list = new ArrayList<ViewScheduleVO>();
        userInfoRepository.findById(uiSeq).orElseThrow(() -> new InvalidInputException("존재하지 않는 회원번호 입니다."));
        if (scheduleInfoRepository.findBySiUiSeq(uiSeq).size()==0) {
            throw new NoSuchElementException();
        }
        for (ScheduleInfoEntity entity : scheduleInfoRepository.findBySiUiSeq(uiSeq)) {
            ViewScheduleVO vo = new ViewScheduleVO(entity);
            list.add(vo);
        }
        return list;
    }

    public ViewScheduleVO addSchedule(AddScheduleVO data) {
        ViewScheduleVO vo = new ViewScheduleVO();
        LocalDateTime sDate = null;
        LocalDateTime eDate = null;
        if (data.getStartDate()!=null) {
            sDate = data.getStartDate();
        }
        if (data.getEndDate()!=null) {
            eDate = data.getEndDate();
        }
        Integer status = 1;
        if (bookInfoRepository.findById(data.getBiSeq()).isEmpty()) {
            throw new InvalidInputException("존재하지 않는 책 번호 입니다.");
        }
        if (sDate!=null) {
            status = 2;
            if (eDate!=null) {
                status = 4;
                // if (eDate.isAfter(LocalDate.now())) {
                //     status = 3;
                // }
                if (status!=1 && sDate.isAfter(eDate)) {
                    throw new InvalidInputException("종료일은 시작일보다 빠를 수 없습니다.");
                }
            }
        }
        vo.setBookTitle(bookInfoRepository.findById(data.getBiSeq()).get().getBiName());
        vo.setDescription(data.getDescription());
        vo.setStartDate(sDate);
        vo.setEndDate(eDate);
        vo.setStatus(status);

        ScheduleInfoEntity entity = ScheduleInfoEntity.builder()
                .siContent(data.getDescription())
                .siStartDate(sDate)
                .siEndDate(eDate)
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
        LocalDateTime sDate = null;
        LocalDateTime eDate = null;
        if (data.getStartDate()!=null) {
            sDate = data.getStartDate();
        }
        if (data.getEndDate()!=null) {
            eDate = data.getEndDate();
        }
        Integer status = 1;
        if (data.getStartDate()!=null) {
            status = 2;
            if (data.getEndDate()!=null) {
                if (status!=1 && sDate.isAfter(eDate)) {
                    throw new InvalidInputException("종료일은 시작일보다 빠를 수 없습니다.");
                }
                status = 4;
            }
        }
        ScheduleInfoEntity newEntity = ScheduleInfoEntity.builder()
                .siSeq(data.getSiSeq())
                .siContent(data.getSiContent())
                .siStartDate(sDate)
                .siEndDate(eDate)
                .siUiSeq(uiSeq).siBiSeq(biSeq)
                .siStatus(status).build();
        scheduleInfoRepository.save(newEntity);

        ViewScheduleVO responseVO = new ViewScheduleVO();
        responseVO.setSiSeq(newEntity.getSiSeq());
        responseVO.setBookTitle(bookInfoRepository.findByBiSeq(newEntity.getSiBiSeq()).getBiName());
        responseVO.setDescription(newEntity.getSiContent());
        responseVO.setStartDate(newEntity.getSiStartDate());
        responseVO.setEndDate(newEntity.getSiEndDate());
        responseVO.setStatus(status);
        return responseVO;
    }
}
