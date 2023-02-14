package com.readers.be3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OneCommentDeleteRequest {
    @Schema(description = "유저 번호")
    private Long userSeq;
    @Schema(description = "한줄평번호")
    private Long oneCommentSeq;
  
}
