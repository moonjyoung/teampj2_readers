package com.readers.be3.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.image.BookImgEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.image.BookImgRepository;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.book.BookInfoImgVO;
import com.readers.be3.vo.book.ResponseBookInfoVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookInfoRepository bookInfoRepository;
    private final BookImgRepository bookImgRepository;
    @Value("${file.image.book}") String book_img_path;

    public ResponseBookInfoVO addBookInfo(BookInfoImgVO data) {
        BookInfoEntity entity = BookInfoEntity.builder()
                .biName(data.getBiName())
                .biAuthor(data.getBiAuthor())
                .biPublisher(data.getBiPublisher())
                .biPage(data.getBiPage())
                .biIsbn(data.getBiIsbn()).build();
        
        String originalFileName = data.getImg().getOriginalFilename();
        String[] split = originalFileName.split("\\.");
        String ext = split[split.length - 1];
        String filename = "";
        for (int i=0; i<split.length-1; i++) {
            filename += split[i];
        }
        String saveFilename = "book_" + Calendar.getInstance().getTimeInMillis() + "." + ext;
        
        Path forderLocation = Paths.get(book_img_path);
        Path targetFile = forderLocation.resolve(saveFilename);
        
        try {
            Files.copy(data.getImg().getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
        
        bookInfoRepository.save(entity);

        BookImgEntity imgEntity = BookImgEntity.builder()
                .bimgFilename(saveFilename)
                .bimgUri(filename)
                .bimgBiSeq(entity.getBiSeq()).build();
        bookImgRepository.save(imgEntity);

        ResponseBookInfoVO vo = new ResponseBookInfoVO(data);
        return vo;
    }
    
    public Map<String, Object> searchBookInfo(String keyword) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<ResponseBookInfoVO> list = new ArrayList<ResponseBookInfoVO>();

        if (keyword==null) {
            keyword = "";
        }
        for (BookInfoEntity data : bookInfoRepository.findAllByBiNameContains(keyword)) {
            // nullpointexception 회피코드. 나중에 check해서 수정
            if (data.getBiSeq()==null || bookImgRepository.findByBimgBiSeq(data.getBiSeq())==null) {
                continue;
            }
            ResponseBookInfoVO vo = new ResponseBookInfoVO();
            vo.setBiSeq(data.getBiSeq());
            vo.setBiName(data.getBiName());
            vo.setBiAuthor(data.getBiAuthor());
            vo.setBiPublisher(data.getBiPublisher());
            vo.setBiPage(data.getBiPage());
            vo.setBiIsbn(data.getBiIsbn());
            vo.setBimgUri(bookImgRepository.findByBimgBiSeq(data.getBiSeq()).getBimgUri());
            list.add(vo);
        }

        resultMap.put("status", true);
        resultMap.put("message", "검색 완료");
        resultMap.put("code", HttpStatus.OK);
        resultMap.put("list", list);
        return resultMap;
    }



    public Map<String, Object> addBookImg(MultipartFile file) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        String originalFileName = file.getOriginalFilename();
        String[] split = originalFileName.split("\\.");
        String ext = split[split.length - 1];
        String filename = "";
        for (int i=0; i<split.length-1; i++) {
            filename += split[i];
        }
        String saveFilename = "book_" + Calendar.getInstance().getTimeInMillis() + "." + ext;
        
        Path forderLocation = Paths.get(book_img_path);
        Path targetFile = forderLocation.resolve(saveFilename);
        
        try {
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e) {
            resultMap.put("status", false);
            resultMap.put("message", "파일 전송에 실패했습니다..");
            resultMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
            return resultMap;
        }

        BookImgEntity data = BookImgEntity.builder()
                .bimgFilename(saveFilename)
                .bimgUri(filename).build();
        
        bookImgRepository.save(data);

        resultMap.put("status", true);
        resultMap.put("message", "새로운 책 이미지가 등록됐습니다.");
        resultMap.put("code", HttpStatus.OK);
        return resultMap;
    }

    public String getFilenameByUri(String uri) {
        BookImgEntity img = bookImgRepository.findTopByBimgUri(uri);
        return img.getBimgFilename();
    }

    // 책 이미지도 따로 업로드 하지 않을 경우
    // public Map<String, Object> addBookInfoTest(BookInfoImgUrlVO data) {
    //     Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

    //     BookInfoTestEntity entity = BookInfoTestEntity.builder()
    //             .bitName(data.getBiName())
    //             .bitAuthor(data.getBiAuthor())
    //             .bitPublisher(data.getBiPublisher())
    //             .bitPage(data.getBiPage())
    //             .bitIsbn(data.getBiIsbn())
    //             .bitCover(data.getBimgUri()).build();
        
    //     testRepository.save(entity);

    //     resultMap.put("status", true);
    //     resultMap.put("message", "새로운 책 정보가 등록됐습니다.");
    //     resultMap.put("code", HttpStatus.OK);
    //     return resultMap;
    // }
}
