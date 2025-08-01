package com.example.demo.favorite.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/findOne")
	public ResponseEntity<FavoriteResponseDto> getFavorite(
		@AuthenticationPrincipal String userId) {
		FavoriteResponseDto result = favoriteService.getMyFavorite(userId);
		return ResponseEntity.ok(result);
	}

	/**
	 * 1) GET /api/favorites/list
	 *    – JWT 검사 → USER 권한 체크 → @AuthenticationPrincipal 으로 userId 주입
	 */
	// 찜한 가게 목록 조회 (페이징)
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/list")
	public ResponseEntity<Page<FavoriteResponseDto>> getFavorites(
		@AuthenticationPrincipal String userId,
		@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<FavoriteResponseDto> result = favoriteService.getMyFavorites(userId, pageable);
		return ResponseEntity.ok(result);
	}

	/**
	 * 2) POST /api/favorites/{storeId}
	 *    – JWT 검사 → USER 권한 체크 → @AuthenticationPrincipal 으로 userId 주입
	 */
	// 가게 찜<->취소 (토글)
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/{storeId}")
	public ResponseEntity<Void> toggleFavorite(
		@AuthenticationPrincipal String userId,
		@PathVariable String storeId
	) {
		favoriteService.toggleFavorite(userId, storeId);
		// 204 No Content: body 없이 “성공” 알림
		return ResponseEntity.noContent().build();
	}

}
