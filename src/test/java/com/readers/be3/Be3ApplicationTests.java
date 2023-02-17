package com.readers.be3;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.repository.ArticleInfoRepository;

import com.readers.be3.repository.BookInfoRepository;

@SpringBootTest
class Be3ApplicationTests {
	@Autowired BookInfoRepository bookInfoRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testSearchBooks() {
		System.out.println(bookInfoRepository.findByBiNameContains("생에").get(0).getBiName());
	}
}
