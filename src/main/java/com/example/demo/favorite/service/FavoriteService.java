package com.example.demo.favorite.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.favorite.dto.FavoriteResponseDto;
import com.example.demo.favorite.entity.FavoriteEntity;
import com.example.demo.favorite.repository.FavoriteRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;

	// 찜한 가게 1건 조회
	public FavoriteResponseDto getMyFavorite(String userId) {
		FavoriteEntity favoriteEntity = favoriteRepository.findByUserUserIdAndDeletedAtIsNull(UUID.fromString(userId));

		FavoriteResponseDto responseDto =
			FavoriteResponseDto.builder()
				.storeId(favoriteEntity.getStore().getStoreId())
				.storeName(favoriteEntity.getStore().getName())
				.build();

		return responseDto;
	}

	// 찜한 가게 목록 조회 (페이징)
	public Page<FavoriteResponseDto> getMyFavorites(String userId, Pageable pageable) {
		UUID uid = UUID.fromString(userId);
		// Page<FavoriteEntity> pageEntity =
		// 	favoriteRepository.findAllByUserUserIdAndDeletedAtIsNull(uid, pageable);

		// COALESCE 를 쓴 쿼리 호출
		Page<FavoriteEntity> pageEntity =
			favoriteRepository.findAllByUserOrderByLastModifiedDesc(uid, pageable);

		// entity -> DTO 맵핑
		return pageEntity.map(entity ->
			FavoriteResponseDto.builder()
				.storeId(entity.getStore().getStoreId())
				.storeName(entity.getStore().getName())
				.build()
		);
	}

}
