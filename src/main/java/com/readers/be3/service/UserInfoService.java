package com.readers.be3.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.readers.be3.entity.ArticleView;
import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.FinishedBookView;
import com.readers.be3.entity.MyPageView;
import com.readers.be3.entity.OneCommentView;
import com.readers.be3.entity.ScheduleInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.entity.image.UserImgEntity;
import com.readers.be3.exception.ErrorResponse;
import com.readers.be3.exception.ReadersProjectException;
import com.readers.be3.repository.ArticleViewRepository;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.FinishedBookViewRepository;
import com.readers.be3.repository.MyPageViewRepository;
import com.readers.be3.repository.OneCommentViewRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.repository.ScheduleInfoRepository;
import com.readers.be3.repository.image.UserImgRepository;
import com.readers.be3.utilities.AESAlgorithm;
import com.readers.be3.vo.mypage.ResponseBookInfoVO;
import com.readers.be3.vo.mypage.ResponseFinishedBookVO;
import com.readers.be3.vo.mypage.ResponseUserArticleVO;
import com.readers.be3.vo.mypage.ResponseUserInfoVO;
import com.readers.be3.vo.mypage.UserImageVO;
import com.readers.be3.vo.mypage.UserInfoVO;
import com.readers.be3.vo.mypage.UserLoginVO;
import com.readers.be3.vo.mypage.UserNameVO;

@Service
public class UserInfoService {
    @Autowired UserImgRepository i_repo;
    @Autowired UserInfoRepository u_repo;
    @Autowired ScheduleInfoRepository scheduleInfoRepository;
    @Autowired MyPageViewRepository v_repo;
    @Autowired ArticleViewRepository a_repo;
    @Autowired OneCommentViewRepository o_repo;
    @Autowired FinishedBookViewRepository f_repo;
    @Autowired BookInfoRepository b_repo;
    
    @Value("${file.image.user}") String user_img_path;

    public Map<String, Object> addUser(UserInfoVO data) { //회원가입
    Map<String ,Object> resultMap = new LinkedHashMap<String, Object>();
    // String name_pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
    String pwd_pattern = "^[a-zA-Z0-9!@#$%^&*()-_=+]*$";

      Long num = Calendar.getInstance().getTimeInMillis();
          
    if(u_repo.countByUiEmail(data.getUiEmail())>=1) { 
        resultMap.put("status", false);
        resultMap.put("message", data.getUiEmail()+"은/는 이미 등록된 이메일 입니다");
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
  UserImgEntity img = i_repo.findByUimgUiSeq(data.getUiSeq());
  

      if(img==null) { //이미지 값이 없을경우 , 이미지를 바로추가

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
    } 
    else { // 이미지 값이 있을경우 데이터를 지우고 이미지추가
      i_repo.delete(img);
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
      
    }
    return resultMap;
  }
  public ResponseUserInfoVO getUserInfo(Long uiSeq) { //마이페이지 정보출력
    MyPageView mView= v_repo.findByUiSeq(uiSeq);
    if (mView == null)
      throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found user %d ", uiSeq)));
    return ResponseUserInfoVO.toResponse(mView);
  }

  public List<ResponseFinishedBookVO> getUserBook(Long uiSeq) { //완독한 책 출력
    List<ResponseFinishedBookVO> list = new ArrayList<ResponseFinishedBookVO>();
    for (ScheduleInfoEntity data : scheduleInfoRepository.findBySiUiSeqAndSiStatus(uiSeq, 4)) {
      if (data == null)
        throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found user %d ", uiSeq)));
      ResponseFinishedBookVO vo = new ResponseFinishedBookVO(data);
      list.add(vo);
    }
    return list;
  }

  public ResponseUserArticleVO getUserArticle(Long uiSeq, Long biSeq) { //마이페이지 유저가 쓴 독후감과 평점 출력
    ArticleView aView = a_repo.findByUiSeqAndBiSeq(uiSeq, biSeq);
    OneCommentView oView = o_repo.findByUiSeqAndBiSeq(uiSeq, biSeq);
    // if (aView == null)
    //   throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found user %d ", uiSeq)));
    // else if (oView == null)
    //   throw new ReadersProjectException(ErrorResponse.of(HttpStatus.NOT_FOUND, String.format("not found book %d ", biSeq)));
    // else 
      return ResponseUserArticleVO.toResponse(aView, oView);
  }
}

