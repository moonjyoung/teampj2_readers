package com.readers.be3.vo.mypage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestUserVO {
    @Schema(description = "유저번호")
    private Long userSeq;
    @Schema(description = "상태", example = "true")  
    private boolean status;
    @Schema(description = "메세지", example = "인증에 성공했습니다")  
    private String message;
}


