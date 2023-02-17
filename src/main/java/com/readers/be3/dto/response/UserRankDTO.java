package com.readers.be3.dto.response;

import com.readers.be3.entity.UserRankView;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRankDTO {
    private Long rank;
    private String userName;
    private Integer totalPage;
    private Integer totalPoint;
    private Integer totalBook;

    public static UserRankDTO toResponse(UserRankView userRankView){
      return new UserRankDTO(userRankView.getRankSeq(),
      userRankView.getUiNickName(),
      userRankView.getUiTotalPage(),
      userRankView.getUiPoint(),
      userRankView.getUiTotalBook());
    }
}
