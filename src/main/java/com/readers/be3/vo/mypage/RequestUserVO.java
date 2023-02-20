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
    @Schema(description = "상태", example = "실패")  
    private boolean status;
    @Schema(description = "메세지", example = "잘못된 접근입니다")  
    private String message;
}


