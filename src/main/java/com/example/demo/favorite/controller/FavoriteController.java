package com.example.demo.favorite.controller;

import java.util.List;
import java.util.UUID;

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
	public ResponseEntity<FavoriteResponseDto> getFavorites(@AuthenticationPrincipal String userId) {
		FavoriteResponseDto result = favoriteService.getMyFavorites(userId);
		return ResponseEntity.ok(result);
	}

	// 찜한 가게 목록 조회

	// 가게 찜 하기
	// 가게 찜 취소하기

}
