package com.readers.be3.vo.schedule;

import java.time.LocalDateTime;

import com.readers.be3.entity.ScheduleInfoEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "일정 입력 결과 리턴 양식")
public class ViewScheduleVO {
    // @Schema(description = "회원 번호", example = "2")
    // private Long uiSeq;
    // @Schema(description = "책 번호", example = "8")
    // private Long biSeq;
    @Schema(description = "일정 고유 번호")
    private Long id;
    @Schema(description = "일정 이름(책 제목)", example = "생에 감사해")
    private String title;
    @Schema(description = "시작 일", example = "2023-02-07 01:23:45")
    private LocalDateTime start;
    @Schema(description = "종료 일(완독 일)", example = "2023-02-14 01:23:45")
    private LocalDateTime end;
    @Schema(description = "개인 작성", example = "감동적이었습니다.")
    private String description;
    @Schema(description = "완독 여부(1.읽기전, 3.독서계획, 4.완독)", example = "3")
    private Integer status;

    public ViewScheduleVO(ScheduleInfoEntity entity) {
        this.id = entity.getSiSeq();
        this.title = entity.getBookInfoEntity().getBiName();
        this.start = entity.getSiStartDate();
        this.end = entity.getSiEndDate();
        this.description = entity.getSiContent();
        this.status = entity.getSiStatus();
    }
    public ViewScheduleVO() {}
}
