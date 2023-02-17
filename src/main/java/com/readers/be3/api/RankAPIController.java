package com.readers.be3.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.RankService;



@RestController
@RequestMapping("/api/rank")
public class RankAPIController {
  

  @Autowired RankService rankService;

  @GetMapping("/list")
  public ResponseEntity<Object> getList(@RequestParam Integer page){
    Sort sort2 = Sort.by("uiPoint").descending();
    Pageable pageable = PageRequest.of(page, 10, sort2);
    return new ResponseEntity<>(rankService.getList(pageable),HttpStatus.OK); 
  }

  @GetMapping("/my")
  public ResponseEntity<Object> getMyRank(@RequestParam Long uiSeq){
    return new ResponseEntity<>(rankService.getMyRank(uiSeq),HttpStatus.OK);
  }
}
