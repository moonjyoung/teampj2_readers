package com.readers.be3.vo.article.responseVO;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleResponseVO {
    @Schema(description = "상태", example = "성공")  
    private boolean status;
    @Schema(description = "메세지", example = "게시글이 등록되었습니다")  
    private String message;
    @Schema(description = "httpStatusCode", example = "OK")  
    private HttpStatus code;

}
