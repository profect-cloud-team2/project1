package com.example.demo.favorite.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
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
import com.example.demo.favorite.dto.FavoriteCountResponseDto;
import com.example.demo.favorite.service.FavoriteService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/favorite")
@SecurityRequirement(name = "bearerAuth")

public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	// 찜한 가게 1건 조회
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/findOne")
	public ResponseEntity<FavoriteResponseDto> getFavorite(
		@AuthenticationPrincipal UserEntity user) {
		FavoriteResponseDto result = favoriteService.getMyFavorite(user.getUserId());
		return ResponseEntity.ok(result);
	}

	/**
	 * 1) GET /api/favorite/list
	 *    – JWT 검사 → USER 권한 체크 → @AuthenticationPrincipal 으로 userId 주입
	 */
	// 찜한 가게 목록 조회 (페이징)
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/list")
	public ResponseEntity<Page<FavoriteResponseDto>> getFavorites(
		@AuthenticationPrincipal UserEntity user,
		@ParameterObject
		@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<FavoriteResponseDto> result = favoriteService.getMyFavorites(user.getUserId(), pageable);
		return ResponseEntity.ok(result);
	}

	/**
	 * 2) POST /api/favorite/{storeId}
	 *    – JWT 검사 → USER 권한 체크 → @AuthenticationPrincipal 으로 userId 주입
	 */
	// 가게 찜<->취소 (토글)
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/{storeId}")
	public ResponseEntity<Void> toggleFavorite(
		@AuthenticationPrincipal UserEntity user,
		@PathVariable String storeId
	) {
		favoriteService.toggleFavorite(user.getUserId(), storeId);
		// 204 No Content: body 없이 “성공” 알림
		return ResponseEntity.noContent().build();
	}

	// 3) 사장이 본인 특정 가게 찜 수 조회
	@PreAuthorize("hasRole('OWNER')")
	@GetMapping("/{storeId}/count")
	public ResponseEntity<FavoriteCountResponseDto> getStoreFavoriteCount(
		@AuthenticationPrincipal UserEntity user,
		@PathVariable UUID storeId
	) {
		long count = favoriteService.getFavoriteCount(user.getUserId(), storeId);
		return ResponseEntity.ok(new FavoriteCountResponseDto(storeId, count));
	}

}
