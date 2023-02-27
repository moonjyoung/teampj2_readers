package com.readers.be3.vo.mypage;

import java.time.LocalDateTime;

import com.readers.be3.entity.ArticleView;
import com.readers.be3.entity.OneCommentView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseUserArticleVO {
    @Schema(description = "독후감 제목" , example = "어린왕자를 읽고")
    private String title;
    @Schema(description = "독후감 내용" , example = "번역이 너무 잘되있고 , 재밌는 책이었습니다")
    private String content;
    @Schema(description = "독후감 날짜" , example = "2023-02-16 17:00:00")
    private LocalDateTime articleTime;
    
    @Schema(description = "한줄평 내용" , example = "재밌어요")
    private String comment;
    @Schema(description = "한줄평 별점" , example = "5")
    private Integer score;
    @Schema(description = "한줄평 날짜" , example = "2023-02-16 17:00:00")
    private LocalDateTime commentTime;

    // private String title;

    // private String uri;


// 게시판 제목, 게시판 내용, 게시판 날짜
// 한줄평 내용, 한줄평 별점, 한줄평 날짜

    public static ResponseUserArticleVO toResponse(ArticleView articleView, OneCommentView oneCommentView) {
        // return ResponseUserArticleVO.builder().
        if (articleView==null && oneCommentView==null) {
            return new ResponseUserArticleVO();
        }
        if(articleView == null)
        return new ResponseUserArticleVO(
            // articleView.getAiTitle(), 
            // articleView.getAiContent(), 
            // articleView.getAiRegdt(),
            null, null, null,
            oneCommentView.getOcComment(),
            oneCommentView.getOcScore(),
            oneCommentView.getOcRegdt()
        );
        else if(oneCommentView == null)
        return new ResponseUserArticleVO(
            articleView.getAiTitle(), 
            articleView.getAiContent(), 
            articleView.getAiRegdt(),
            null, null, null
        );
        else
        return new ResponseUserArticleVO(
            articleView.getAiTitle(), 
            articleView.getAiContent(), 
            articleView.getAiRegdt(),
            oneCommentView.getOcComment(),
            oneCommentView.getOcScore(),
            oneCommentView.getOcRegdt()
        );
    }
}

