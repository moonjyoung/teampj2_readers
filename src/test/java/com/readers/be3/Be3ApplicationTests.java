package com.readers.be3;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.readers.be3.entity.ArticleInfoEntity;
import com.readers.be3.repository.ArticleInfoRepository;

@SpringBootTest
class Be3ApplicationTests {
	@Autowired ArticleInfoRepository articleRepo;

	@Test
	void contextLoads() {
	}

	}

