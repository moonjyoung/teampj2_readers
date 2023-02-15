package com.readers.be3.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.UserImgEntity;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.image.UserImgRepository;
import com.readers.be3.utilities.AESAlgorithm;
import com.readers.be3.vo.mypage.UserImageVO;
import com.readers.be3.vo.mypage.UserInfoVO;
import com.readers.be3.vo.mypage.UserLoginVO;
import com.readers.be3.vo.mypage.UserNameVO;

import lombok.RequiredArgsConstructor;

@Service
public class UserInfoService {
    @Autowired UserImgRepository i_repo;
    @Autowired UserInfoRepository u_repo;
    @Value("${file.image.user}") String user_img_path;
    public Map<String, Object> addUser(UserInfoVO data) { //회원가입
    Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
    // String name_pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
    String pwd_pattern = "^[a-zA-Z0-9!@#$%^&*()-_=+]*$";
    
      // int leftLimit = 48; // numeral '0'
      // int rightLimit = 122; // letter 'z'
      // int length = 10;
      // Random random = new Random();
      // String nickname = "user#"+random.ints(leftLimit, rightLimit + 1)
      //     .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
      //     .limit(length)
      //     .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      //     .toString();

      Long num = Calendar.getInstance().getTimeInMillis();
          
    if(u_repo.countByUiEmail(data.getUiEmail())>=1) { 
        resultMap.put("status", false);
        resultMap.put("message", data.getUiEmail()+"은/는 이미 등록된 이메일 입니다");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }

    // else if(u_repo.countByUiNickname(data.getUiNickname())>=1) { 
    //     resultMap.put("status", false);
    //     resultMap.put("message", data.getUiEmail()+"은/는 이미 등록된 닉네임 입니다");
    //     resultMap.put("code", HttpStatus.BAD_REQUEST);
    // }

    else if(data.getUiPwd().length()<8) {
      resultMap.put("status", false);
      resultMap.put("message", "비밀번호는 8자리 이상입니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    }

    else if(!Pattern.matches(pwd_pattern, data.getUiPwd())) {
      resultMap.put("status", false); 
      resultMap.put("message", "비밀번호에 공백문자를 사용 할 수 없습니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
    // else if(!Pattern.matches(name_pattern, data.getUiNickname())) { 
    //   resultMap.put("status", false);
    //   resultMap.put("message", "닉네임에 공백문자나 특수문자를 사용 할 수 없습니다");
    //   resultMap.put("code", HttpStatus.BAD_REQUEST);
    // } 
    else {
      try {
        String encPwd = AESAlgorithm.Encrypt(data.getUiPwd());
        data.setUiPwd(encPwd);
      }  catch(Exception e) {e.printStackTrace();}

      UserInfoEntity member = UserInfoEntity.builder()
        .uiPwd(data.getUiPwd())
        .uiEmail(data.getUiEmail())
        .uiNickname("user#"+ num)
        .build();

        u_repo.save(member);

      resultMap.put("status", true);
      resultMap.put("message", "회원이 등록되었습니다");
      resultMap.put("code", HttpStatus.CREATED);
    }
    return resultMap;
    }

    public Map<String, Object> loginUser(UserLoginVO data) { //로그인
        Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
        UserInfoEntity loginUser = null; 
    try {
      loginUser = u_repo.findTop1ByUiEmailAndUiPwd(
      data.getUiEmail(), AESAlgorithm.Encrypt(data.getUiPwd())
      );
    }catch(Exception e) {e.printStackTrace();}
    if(loginUser == null) {
      resultMap.put("status", false);
      resultMap.put("message", "이메일 또는 비밀번호 오류입니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    }

    else {
      resultMap.put("status", true);
      resultMap.put("message", "로그인 되었습니다");
      resultMap.put("code", HttpStatus.ACCEPTED);
      resultMap.put("loginUser", loginUser);
    }
        return resultMap;
    }

    public Map<String, Object> deleteUser(Long uiSeq) { //회원탈퇴
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        UserInfoEntity login = u_repo.findByUiSeq(uiSeq);
        if(login == null) {
          resultMap.put("status", false);
          resultMap.put("message", "해당 회원이 존재하지 않습니다.");
          resultMap.put("code",HttpStatus.BAD_REQUEST);
        }
        else {
        u_repo.delete(login);
        resultMap.put("status", true);
        resultMap.put("message", "회원정보가 삭제 되었습니다");
        resultMap.put("code",HttpStatus.OK);
        }
    return resultMap;
}

    public Map<String, Object> updateUserName(Long uiSeq, UserNameVO data) { //닉네임 수정
        String name_pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        UserInfoEntity login = u_repo.findByUiSeq(uiSeq);
        if(login == null) {
          resultMap.put("status", false);
          resultMap.put("message", "해당 회원이 존재하지 않습니다.");
          resultMap.put("code",HttpStatus.BAD_REQUEST);
        }
        else if(u_repo.countByUiNickname(data.getUiNickname())>=1&&!login.getUiNickname().equals(data.getUiNickname())) { 
          resultMap.put("status", false);
          resultMap.put("message", data.getUiNickname()+"은/는 이미 등록된 닉네임 입니다");
          resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else if(!Pattern.matches(name_pattern, data.getUiNickname())) {
        resultMap.put("status", false);
        resultMap.put("message", "닉네임에 공백문자나 특수문자를 사용 할 수 없습니다");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
        } 
        else {
        login.setUiNickname(data.getUiNickname());
        u_repo.save(login);
        resultMap.put("status", true);
        resultMap.put("message", "회원정보가 수정 되었습니다");
        resultMap.put("code",HttpStatus.OK);
        }
        return resultMap;
    }

  public Map<String, Object> updateUserPhoto(UserImageVO data) { // 유저 사진 추가
  Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
  UserInfoEntity login = u_repo.findByUiSeq(data.getUiSeq());

  String originalFileName = data.getImg().getOriginalFilename();
  String[] split = originalFileName.split("\\.");
  String ext = split[split.length - 1];
  String filename = "";
  for (int i=0; i<split.length-1; i++) {
    filename += split[i];
  }
  String saveFilename = "user_" + LocalDateTime.now().getNano() + "."+ext;
  
  Path forderLocation = Paths.get(user_img_path);
  Path targetFile = forderLocation.resolve(saveFilename);

  try {
    Files.copy(data.getImg().getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
  }
  catch (Exception e) {
    resultMap.put("status", false);
    resultMap.put("message", "파일 전송에 실패했습니다");
    resultMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
    return resultMap;
  }

  UserImgEntity imgEntity = UserImgEntity.builder()
  .uimgFilename(saveFilename)
  .uimgUri(filename)
  .uimgUiSeq(login.getUiSeq()).build();

  i_repo.save(imgEntity);
  resultMap.put("status", true);
  resultMap.put("message", "사진 등록이 완료되었습니다");
  resultMap.put("code", HttpStatus.OK);
  return resultMap;
  } 
}

