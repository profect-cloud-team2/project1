package com.example.demo.search.controller;

import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.search.exception.SearchException;
import com.example.demo.search.service.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@Operation(summary = "가게 검색", description = "키워드를 기준으로 가게를 검색합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "검색 성공"),
		@ApiResponse(responseCode = "400", description = "검색 키워드가 비어 있거나 유효하지 않음")
	})
	@GetMapping
	public Page<SearchResultDto> search(
		@Parameter(
			description = "검색 키워드 (예: 가게 이름, 카테고리, 주소 일부 포함)",
			example = "강남"
		)
		@RequestParam String keyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "created") String sortBy
	) {
		if (keyword == null || keyword.isBlank()) {
			throw new SearchException();
		}
		return searchService.search(keyword, page, size, sortBy);
	}
}
