package com.readers.be3.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.UserInfoService;
import com.readers.be3.vo.mypage.UserInfoVO;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Value("${file.image.user}") String user_img_path;
    @Autowired UserInfoService uService;
    @PutMapping("/join") //회원가입
    public ResponseEntity<Object> memberJoin(@RequestBody UserInfoVO data) {
      Map<String, Object> resultMap = uService.addUser(data);
      return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }
    @PostMapping("/login") //로그인
    public ResponseEntity<Object> memberLogin(@RequestBody UserInfoVO data) {
      Map<String, Object> resultMap = uService.loginUser(data);
      return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }
    @DeleteMapping("/delete") //회원탈퇴
    public ResponseEntity<Object> memberDelete(@RequestParam Long uiSeq) throws Exception{
    Map <String ,Object> resultMap = uService.deleteUser(uiSeq);
    return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
  }
    @PatchMapping("/update/name") //회원정보수정
    public ResponseEntity<Object> memberNameUpdate(@RequestParam Long uiSeq, @RequestBody UserInfoVO data) throws Exception{
    Map <String ,Object> resultMap = uService.updateUserName(uiSeq, data);
    return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
  }
}
