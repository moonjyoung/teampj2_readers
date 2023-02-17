package com.readers.be3.vo.schedule;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "일정을 수정할 때")
public class UpdateScheduleVO {
    @Schema(description = "일정 고유 번호")
    private Long siSeq;
    @Schema(description = "사용자 작성")
    private String siContent;
    @Schema(description = "일정 시작 일")
    private LocalDate siStartDate;
    @Schema(description = "일정 종료 일")
    private LocalDate siEndDate;
}
