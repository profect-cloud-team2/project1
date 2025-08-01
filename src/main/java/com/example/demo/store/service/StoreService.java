package com.example.demo.store.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreAiService storeAiService;
	private final UserRepository userRepository;

	public Page<SearchResultDto> search(String keyword, int page, int size, String sortBy) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		if ("modified".equalsIgnoreCase(sortBy)) {
			sort = Sort.by(Sort.Direction.DESC, "updatedAt");
		}

		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<StoreEntity> resultPage = storeRepository.searchVisibleStoresByKeyword(keyword, pageable);

		return resultPage.map(SearchResultDto::fromEntity);
	}

	public StoreResponseDto createStore(StoreCreateRequestDto dto, String userId) {
		boolean exists = storeRepository.existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCaseAndDeletedAtIsNull(
			dto.getName().trim(), dto.getAddress1().trim(), dto.getAddress2().trim()
		);
		if (exists) {
			throw new IllegalArgumentException("이미 등록된 가게입니다.");
		}

		String desc = storeAiService.generateAiDescription(
			dto.getName(),
			dto.getCategory()
		);

		UserEntity user = userRepository.findByUserIdAndDeletedAtIsNull(UUID.fromString(userId))
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		StoreEntity entity = StoreEntity.fromCreateDto(dto, user, desc);
		entity.setCreatedBy(user.getUserId());
		StoreEntity saved = storeRepository.save(entity);

		return toResponseDto(saved);
	}


	public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto dto) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 가게가 없습니다."));

		store.updateFromDto(dto);
		String newDesc = storeAiService.generateAiDescription(
			dto.getName(),
			dto.getCategory()  // "한식"
		);
		store.setAiDescription(newDesc);

		StoreEntity updated = storeRepository.save(store);
		return toResponseDto(updated);
	}

	public Optional<StoreEntity> findById(UUID storeId) {
		return storeRepository.findById(storeId);
	}

	private StoreResponseDto toResponseDto(StoreEntity entity) {
		return new StoreResponseDto(
			entity.getStoreId(),
			entity.getUser().getUserId(),
			entity.getName(),
			entity.getBusinessNum(),
			entity.getCategory(),
			entity.getAddress1(),
			entity.getAddress2(),
			entity.getPhoneNum(),
			entity.getImgURL(),
			entity.getOpenTime(),
			entity.getClosedTime(),
			entity.getAiDescription(),
			entity.getIntroduction(),
			entity.getStore_latitude(),
			entity.getStore_longitude(),
			entity.getIsAvailable()
		);
	}
	public void requestStoreClosure(UUID storeId, String userId, String reason) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

		// 소유자 검증
		if (!store.getUser().getUserId().toString().equals(userId)) {
			throw new IllegalArgumentException("본인의 가게만 폐업 신청할 수 있습니다.");
		}

		// 이미 폐업 요청 or 폐업된 상태인지 체크
		if (store.getIsAvailable() == StoreStatus.CLOSED_REQUESTED || store.getIsAvailable() == StoreStatus.CLOSED) {
			throw new IllegalStateException("이미 폐업 요청 중이거나 폐업된 가게입니다.");
		}

		// soft delete 방식으로 상태 변경
		store.softDelete(StoreStatus.CLOSED_REQUESTED, reason, UUID.fromString(userId));
		storeRepository.save(store);
	}
	// 폐업 요청 승인 (관리자 권한)
	public void approveStoreClosure(UUID storeId, String adminId) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

		if (store.getIsAvailable() != StoreStatus.CLOSED_REQUESTED) {
			throw new IllegalStateException("해당 가게는 폐업 요청 상태가 아닙니다.");
		}

		store.softDelete(StoreStatus.CLOSED, "관리자 승인", UUID.fromString(adminId));
		storeRepository.save(store);
	}
	public void rejectStoreClosure(UUID storeId, String adminId, String reason) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

		if (store.getIsAvailable() != StoreStatus.CLOSED_REQUESTED) {
			throw new IllegalStateException("폐업 신청 상태가 아닙니다.");
		}

		// 폐업 신청 거절 → 상태 복구
		store.setIsAvailable(StoreStatus.OPEN);
		store.setIntroduction("[폐업 거절] " + reason);
		store.setDeletedAt(null);
	}
	// 관리자 강제 삭제
	public void forceDeleteStore(UUID storeId, String adminId, String reason) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

		store.softDelete(StoreStatus.DELETED, reason, UUID.fromString(adminId));
		storeRepository.save(store);
	}
}
