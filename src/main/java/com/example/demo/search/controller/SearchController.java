package com.example.demo.search.controller;

import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.search.exception.SearchException;
import com.example.demo.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@GetMapping
	public Page<SearchResultDto> search(
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
