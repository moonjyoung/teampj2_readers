package com.readers.be3.vo.article;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostWriterCommentVO {
    @Schema(description = "한줄평 내용", example = "testContent", required = true)
    private String acContent;
    @Schema(description = "한줄평을 달 게시글의 번호", example = "1", required = true)
    private Long acAiSeq;
    @Schema(description = "현재 로그인된 사용자의 번호 ", example = "1", required = true)
    private Long acUiSeq;
}
