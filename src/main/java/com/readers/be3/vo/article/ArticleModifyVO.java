package com.readers.be3.vo.article;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

// 게시글 수정할 데이터
@Data
public class ArticleModifyVO {

private String aiTitle;
private String aiContent;
private Integer aiPublic;
private List<MultipartFile> files;
}
