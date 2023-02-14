package com.readers.be3.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.UserImgEntity;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.utilities.AESAlgorithm;
import com.readers.be3.vo.mypage.UserImageVO;
import com.readers.be3.vo.mypage.UserInfoVO;

@Service
public class UserInfoService {
    @Autowired UserInfoRepository u_repo;
    public Map<String, Object> addUser(UserInfoVO data) { //회원가입
    Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
    String name_pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
    String pwd_pattern = "^[a-zA-Z0-9!@#$%^&*()-_=+]*$";
    if(u_repo.countByUiEmail(data.getUiEmail())>=1) { 
        resultMap.put("status", false);
        resultMap.put("message", data.getUiEmail()+"은/는 이미 등록된 이메일 입니다");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }
    else if(u_repo.countByUiNickname(data.getUiNickname())>=1) { 
        resultMap.put("status", false);
        resultMap.put("message", data.getUiEmail()+"은/는 이미 등록된 닉네임 입니다");
        resultMap.put("code", HttpStatus.BAD_REQUEST);
    }

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
    else if(!Pattern.matches(name_pattern, data.getUiNickname())) {
      resultMap.put("status", false);
      resultMap.put("message", "닉네임에 공백문자나 특수문자를 사용 할 수 없습니다");
      resultMap.put("code", HttpStatus.BAD_REQUEST);
    } 
    else {
      try {
        String encPwd = AESAlgorithm.Encrypt(data.getUiPwd());
        data.setUiPwd(encPwd);
      }  catch(Exception e) {e.printStackTrace();}

      UserInfoEntity member = UserInfoEntity.builder()
        .uiPwd(data.getUiPwd())
        .uiEmail(data.getUiEmail())
        .uiNickname(data.getUiNickname())
        // .uiRegDt(data.getUiRegdt())
        // .uiPoint(data.getUiPoint())
        .build();

        u_repo.save(member);

      resultMap.put("status", true);
      resultMap.put("message", "회원이 등록되었습니다");
      resultMap.put("code", HttpStatus.CREATED);
    }
    return resultMap;
    }

    public Map<String, Object> loginUser(UserInfoVO data) { //로그인
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

    public Map<String, Object> updateUserName(Long uiSeq, UserInfoVO data) { //닉네임 수정
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

  //   public Map<String , Object> updateUserPhoto(Long uiSeq,UserImageVO data, MultipartFile img) { 
  //     Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
  //     String savedFilePath = "";
  //     try {
  //         savedFilePath = fileService.saveImageFile("artist_group", img);
  //     }
  //     catch(Exception e) {
  //         System.out.println("파일 전송 실패");
  //         resultMap.put("status", false);
  //         resultMap.put("message", "파일 전송에 실패했습니다");
  //         resultMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
  //         return resultMap;
  //     }
      
  //     ArtistGroupInfoEntity entity = ArtistGroupInfoEntity.builder()
  //             .agiName(data.getName())
  //             .agiDebutYear(data.getDebutYear())
  //             .company(companyRepository.findById(data.getCompany()).get())
  //             .agiImg(savedFilePath)
  //             .build();
  //     agiRepo.save(entity);

  //     resultMap.put("status", true);
  //     resultMap.put("message", "사진 수정이 완료 되었습니다");
  //     resultMap.put("code", HttpStatus.OK);

  //     return resultMap;
  // }
}
