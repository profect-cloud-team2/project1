package com.example.demo.search;

import com.example.demo.global.exception.GlobalExceptionHandler;
import com.example.demo.search.controller.SearchController;
import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.search.exception.SearchException;
import com.example.demo.search.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SearchControllerTest {

	@InjectMocks
	private SearchController searchController;

	@Mock
	private SearchService searchService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(searchController)
			.setControllerAdvice(new GlobalExceptionHandler())
			.alwaysDo(print())
			.build();
	}

	@Test
	void 검색_성공() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		when(searchService.search("떡볶이", 0, 10, "created"))
			.thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

		mockMvc.perform(get("/api/search")
				.param("keyword", "떡볶이")
				.param("page", "0")
				.param("size", "10")
				.param("sortBy", "created"))
			.andExpect(status().isOk());
	}

	@Test
	void 검색어없을때_예외발생() throws Exception {
		mockMvc.perform(get("/api/search")
				.param("keyword", "") // 빈 문자열
				.param("page", "0")
				.param("size", "10")
				.param("sortBy", "created"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("SEARCH_ERROR"))
			.andExpect(jsonPath("$.message").value("검색어를 입력해주세요."));	}
}
