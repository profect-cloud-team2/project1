package com.example.demo.favorite.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.favorite.dto.FavoriteResponseDto;
import com.example.demo.favorite.service.FavoriteService;

@RestController
@RequestMapping("/api/favorite")

public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	// 찜한 가게 1건 조회
	@GetMapping("/findOne")
	public ResponseEntity<FavoriteResponseDto> getFavorite(@AuthenticationPrincipal String userId) {
		FavoriteResponseDto result = favoriteService.getMyFavorite(userId);
		return ResponseEntity.ok(result);
	}

	// 찜한 가게 목록 조회 (페이징)
	@GetMapping("/list")
	public ResponseEntity<Page<FavoriteResponseDto>> getFavorites(
		@AuthenticationPrincipal String userId,
		@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<FavoriteResponseDto> result = favoriteService.getMyFavorites(userId, pageable);
		return ResponseEntity.ok(result);
	}

	// 가게 찜 하기
	// 가게 찜 취소하기

}
