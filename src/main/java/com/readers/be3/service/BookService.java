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
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.readers.be3.entity.BookInfoAladinEntity;
import com.readers.be3.entity.image.BookImgEntity;
import com.readers.be3.repository.BookInfoAladinRepository;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.image.BookImgRepository;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.book.BookInfoAladinVO;
import com.readers.be3.vo.book.ResponseBookInfoVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookInfoAladinRepository bookInfoAladinRepository;
    private final BookInfoRepository bookInfoRepository;
    private final BookImgRepository bookImgRepository;
    @Value("${file.image.book}") String book_img_path;

    public ResponseBookInfoVO addBookInfo(BookInfoAladinVO data) {
        BookInfoAladinEntity entity = BookInfoAladinEntity.builder()
                .biName(data.getBiName())
                .biAuthor(data.getBiAuthor())
                .biPublisher(data.getBiPublisher())
                .biPage(data.getBiPage())
                .biIsbn(data.getBiIsbn())
                .biUri(data.getBimgUri()).build();
        bookInfoAladinRepository.save(entity);
        ResponseBookInfoVO vo = new ResponseBookInfoVO(data);
        vo.setBiSeq(entity.getBiSeq());
        return vo;
    }

    public List<ResponseBookInfoVO> searchBookInfo(String keyword, Integer sortNo) {
        List<ResponseBookInfoVO> list = new ArrayList<ResponseBookInfoVO>();
        Sort sort = sortBySortNo(sortNo);
        if (bookInfoAladinRepository.findByBiNameContainsOrBiAuthorContainsOrBiPublisherContains(keyword, keyword, keyword, sort).size()==0) {
            throw new NoSuchElementException();
        }
        else {
            for (BookInfoAladinEntity data : bookInfoAladinRepository.findByBiNameContainsOrBiAuthorContainsOrBiPublisherContains(keyword, keyword, keyword, sort)) {
                ResponseBookInfoVO vo = new ResponseBookInfoVO(data);
                list.add(vo);
            }
        }
        return list;
    }

    private Sort sortBySortNo(Integer sortNo) {
        if (sortNo==null || sortNo==1) {
            return Sort.by(Sort.Direction.DESC, "biSeq");
        }
        else if (sortNo==2) {
            return Sort.by(Sort.Direction.DESC, "biName");
        }
        else {
            throw new InvalidInputException("유효하지 않은 검색조건 입니다.");
        }
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
        if (img==null) {
            throw new InvalidInputException("존재하지 않는 파일입니다.");
        }
        return img.getBimgFilename();
    }

    // 이미지 파일 업로드가 필요없고 알라딘에 있는 이미지 주소를 땡겨오면 되는 경우
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
