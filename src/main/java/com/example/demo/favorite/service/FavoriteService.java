package com.example.demo.favorite.service;

import java.util.NoSuchElementException;
import java.util.UUID;

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

	public FavoriteResponseDto getMyFavorites(String userId) {
		FavoriteEntity favoriteEntity = favoriteRepository.findByUserUserIdAndDeletedAtIsNull(UUID.fromString(userId));

		FavoriteResponseDto responseDto =
			FavoriteResponseDto.builder()
				.storeId(favoriteEntity.getStore().getStoreId())
				.storeName(favoriteEntity.getStore().getName())
				.build();

		return responseDto;
	}
}
