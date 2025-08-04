package com.example.demo.favorite.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.favorite.dto.FavoriteCountResponseDto;
import com.example.demo.favorite.dto.FavoriteResponseDto;
import com.example.demo.favorite.entity.FavoriteEntity;
import com.example.demo.favorite.repository.FavoriteRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final StoreService storeService;

	// 찜한 가게 1건 조회
	@Transactional(readOnly = true)
	public FavoriteResponseDto getMyFavorite(UUID userId) {
		FavoriteEntity favoriteEntity = favoriteRepository.findByUserUserIdAndDeletedAtIsNull(userId);

		FavoriteResponseDto responseDto =
			FavoriteResponseDto.builder()
				.storeId(favoriteEntity.getStore().getStoreId())
				.storeName(favoriteEntity.getStore().getName())
				.build();

		return responseDto;
	}

	// 찜한 가게 목록 조회 (페이징)
	@Transactional(readOnly = true)
	public Page<FavoriteResponseDto> getMyFavorites(UUID userId, Pageable pageable) {
		// Page<FavoriteEntity> pageEntity =
		// 	favoriteRepository.findAllByUserUserIdAndDeletedAtIsNull(uid, pageable);

		// COALESCE 를 쓴 쿼리 호출
		Page<FavoriteEntity> pageEntity =
			favoriteRepository.findAllByUserOrderByLastModifiedDesc(userId, pageable);

		// entity -> DTO 맵핑
		return pageEntity.map(entity ->
			FavoriteResponseDto.builder()
				.storeId(entity.getStore().getStoreId())
				.storeName(entity.getStore().getName())
				.build()
		);
	}

	@Transactional
	public void toggleFavorite(UUID userId, String storeId) {
		UUID storeid = UUID.fromString(storeId);

		// 1) 스토어가 존재하는지 체크(404)
		StoreEntity store = storeService.findById(storeid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."));

		// 2) 기존 찜 정보 조회
		Optional<FavoriteEntity> opt = favoriteRepository.findByUserUserIdAndStoreStoreId(userId, storeid);

		if (opt.isEmpty()) {
			// 2-1) 기록이 없으면 신규 생성 (신규 생성만 save() 호출)
			UserEntity user = userRepository.getById(userId);
			FavoriteEntity newFavorite = FavoriteEntity.builder()
				// .favoriteId(UUID.randomUUID())
				.user(user)
				.store(store)
				.createdBy(userId)
				.build();
			favoriteRepository.save(newFavorite);  // persist() → INSERT
			return;
		}

		FavoriteEntity favorite = opt.get();
		if (favorite.getDeletedAt() == null) {
			// 2-2) 활성화 상태라면 -> soft-delete (취소)
			favorite.setDeletedAt(LocalDateTime.now());
			favorite.setDeletedBy(userId);
		} else {
			// 2-3) soft-deleted 상태라면 -> 복구
			favorite.setDeletedAt(null);
			favorite.setDeletedBy(null);
			// favorite.setUpdatedBy(uid);
		}
		// 변경이 감지되어 저장됨 (JPA dirty-checking)
	}

	/**
	 * (사장 전용) 본인이 소유한 storeId의 찜 수를 조회.
	 * 소유가 아니면 AccessDeniedException 발생
	 */
	@Transactional
	public long getFavoriteCount(UUID userId, UUID storeId) {
		// UUID storeid = UUID.fromString(storeId);
		// 1) 소유권 확인
		boolean isOwner = storeRepository.existsByStoreIdAndUserUserId(storeId, userId);
		if (!isOwner) {
			throw new AccessDeniedException(
				String.format("userId=%s 가 storeId=%s 의 소유자가 아닙니다.", userId, storeId)
			);
		}
		// 2) 찜 개수 조회 (soft-delete 고려)
		return favoriteRepository.countByStoreStoreIdAndDeletedAtIsNull(storeId);
	}
}
