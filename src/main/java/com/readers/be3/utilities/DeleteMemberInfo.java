package com.readers.be3.utilities;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.repository.UserInfoRepository;

import java.util.*;

@Component
public class DeleteMemberInfo {
    @Autowired UserInfoRepository u_repo;
    
    @Scheduled(cron = "0 0 12 * * *") //매일 12시 0분 00초 실행
    public void autoDelete() {
        
        Integer leaveMember = u_repo.countByUiStatus(2);
        
        for(int i = 0 ; i <= leaveMember ; i++) {
            UserInfoEntity member = u_repo.findTop1ByUiStatus(2);
                if(member != null) {
                    Date day = new Date();
                    Calendar today = Calendar.getInstance();
                    today.setTime(day); 
                    
                    Calendar leaveDt = Calendar.getInstance();
                    
                    leaveDt.setTime(member.getUiLeaveDt());
                    Long diffSec = (today.getTimeInMillis() - leaveDt.getTimeInMillis())/1000;
                    Long diffDay = diffSec / (24 * 60 * 60);
                    
                    if(diffDay >= 180) {
                        u_repo.delete(member);
                    }
                }
                else if(member == null) {
                    break;
                } 
             }
        }
     }

