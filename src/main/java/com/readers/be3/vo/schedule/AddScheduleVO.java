package com.readers.be3.vo.schedule;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "일정을 추가할 때 ")
public class AddScheduleVO {
    @Schema(description = "회원 번호", example = "2")
    private Long uiSeq;
    @Schema(description = "책 번호", example = "8")
    private Long biSeq;
    @Schema(description = "시작 일(nullable)", example = "2023-02-07")
    private LocalDate startDate;
    @Schema(description = "종료 일(완독 일, nullable)", example = "2023-02-14")
    private LocalDate endDate;
    @Schema(description = "개인 작성(nullable)", example = "감동적이었습니다.")
    private String description;
    @Schema(description = "완독 여부(1.읽기전, 2.읽는중, 3.중단. 4.완독)", example = "4")
    private Integer status;
}
