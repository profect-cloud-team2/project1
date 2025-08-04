package com.example.demo.store.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.ai.entity.AiEntity;
import com.example.demo.ai.repository.AiRepository;
import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.store.dto.AiResponseDto;
import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.exception.*;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreAiService storeAiService;
	private final UserRepository userRepository;
	private final AiRepository aiRepository;

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

	public Page<StoreResponseDto> getStoresByStatusUnderReview(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

		Page<StoreEntity> storePage = storeRepository.findAllByIsAvailable(StoreStatus.UNDER_REVIEW, pageable);

		return storePage.map(this::toResponseDto);
	}


	public void approveStore(UUID storeId, UUID adminId) {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		store.setIsAvailable(StoreStatus.OPEN);
		store.setUpdatedBy(adminId);
		store.setUpdatedAt(LocalDateTime.now());

		storeRepository.save(store);
	}

	public void rejectStore(UUID storeId, String reason, UUID adminId) {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(StoreNotFoundException::new);

		store.setIsAvailable(StoreStatus.DELETED);
		store.setIntroduction("[등록 거절] " + reason);
		store.setUpdatedBy(adminId);
		store.setUpdatedAt(LocalDateTime.now());

		storeRepository.save(store);
	}


	public StoreResponseDto createStore(StoreCreateRequestDto dto, UserEntity user) {
		boolean exists = storeRepository.existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCaseAndDeletedAtIsNull(
			dto.getName().trim(), dto.getAddress1().trim(), dto.getAddress2().trim()
		);
		if (exists) {
			throw new StoreAlreadyExistsException();
		}

		AiResponseDto aiResponseDto = storeAiService.generateAiDescription(dto.getName(), dto.getCategory());

		// AI 호출 로그 저장
		AiEntity log = AiEntity.builder()
			.apiType("STORE_DESC")
			.requestJson(aiResponseDto.getRequest())
			.responseJson(aiResponseDto.getResponse())
			.createdBy(user.getUserId())
			.build();
		aiRepository.save(log);

		StoreEntity entity = StoreEntity.fromCreateDto(dto, user, aiResponseDto.getResponse());
		entity.setCreatedBy(user.getUserId());
		StoreEntity saved = storeRepository.save(entity);

		return toResponseDto(saved);
	}

	public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto dto, UserEntity user) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (!store.getUser().getUserId().equals(user.getUserId())) {
			throw new UnauthorizedStoreAccessException();
		}

		if (dto.getIsAvailable() != null) {
			if (dto.getIsAvailable() != StoreStatus.OPEN && dto.getIsAvailable() != StoreStatus.PREPARE) {
				throw new InvalidStoreStatusException();
			}
			store.setIsAvailable(dto.getIsAvailable());
		}

		store.updateFromDto(dto);

		AiResponseDto newAiResponseDto = storeAiService.generateAiDescription(dto.getName(), dto.getCategory());

		// AI 호출 로그 저장
		AiEntity log = AiEntity.builder()
			.apiType("STORE_DESC")
			.requestJson(newAiResponseDto.getRequest())
			.responseJson(newAiResponseDto.getResponse())
			.createdBy(user.getUserId())
			.build();
		aiRepository.save(log);

		store.setAiDescription(newAiResponseDto.getResponse());

		store.setUpdatedBy(user.getUserId());
		store.setUpdatedAt(LocalDateTime.now());

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

	public void requestStoreClosure(UUID storeId, UserEntity user, String reason) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (!store.getUser().getUserId().equals(user.getUserId())) {
			throw new UnauthorizedStoreAccessException();
		}

		if (store.getIsAvailable() == StoreStatus.CLOSED_REQUESTED || store.getIsAvailable() == StoreStatus.CLOSED) {
			throw new StoreClosureStateException();
		}

		store.softDelete(StoreStatus.CLOSED_REQUESTED, reason, user.getUserId());
		storeRepository.save(store);
	}

	public void approveStoreClosure(UUID storeId, UserEntity admin) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (store.getIsAvailable() != StoreStatus.CLOSED_REQUESTED) {
			throw new StoreClosureStateException();
		}

		store.softDelete(StoreStatus.CLOSED, "관리자 승인", admin.getUserId());
		storeRepository.save(store);
	}

	public void rejectStoreClosure(UUID storeId, UserEntity admin, String reason) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(StoreNotFoundException::new);

		if (store.getIsAvailable() != StoreStatus.CLOSED_REQUESTED) {
			throw new StoreClosureStateException();
		}

		store.setIsAvailable(StoreStatus.OPEN);
		store.setIntroduction("[폐업 거절] " + reason);
		store.setDeletedAt(null);
	}

	public void forceDeleteStore(UUID storeId, UserEntity admin, String reason) {
		StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
			.orElseThrow(StoreNotFoundException::new);

		store.softDelete(StoreStatus.DELETED, reason, admin.getUserId());
		storeRepository.save(store);
	}
}
