package com.readers.be3.vo.article;

import lombok.Builder;
import lombok.Data;
// 게시글 이미지 받아올 VO
@Data
@Builder
public class articleImgVO {
private String aimgFilename;
private String aimgUri;
}
