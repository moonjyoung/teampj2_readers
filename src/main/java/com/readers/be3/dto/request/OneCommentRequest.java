package com.readers.be3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OneCommentRequest {
  @Schema(description = "유저 번호")
  private Long userSeq;
  @Schema(description = "책번호")
  private Long bookSeq;
  @Schema(description = "한줄평 내용")
  private String comment;
  @Schema(description = "한줄평 점수")
  private Integer score;
}
