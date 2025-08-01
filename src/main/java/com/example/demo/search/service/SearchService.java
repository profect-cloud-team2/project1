package com.example.demo.search.service;

import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final StoreRepository storeRepository;

	public Page<SearchResultDto> search(String keyword, int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, convertSortKey(sortBy)));

		Page<StoreEntity> storePage = storeRepository.searchVisibleStoresByKeyword(keyword, pageable);

		List<SearchResultDto> resultDtoList = storePage.stream()
			.map(SearchResultDto::fromEntity)
			.toList();

		return new PageImpl<>(resultDtoList, pageable, storePage.getTotalElements());
	}

	private String convertSortKey(String sortBy) {
		return switch (sortBy.toLowerCase()) {
			case "name" -> "name";
			case "created" -> "createdAt";
			default -> "createdAt"; // 기본값
		};
	}
}
