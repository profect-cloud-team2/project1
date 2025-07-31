package com.example.demo.store.service;

import java.util.Optional;
import java.util.UUID;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreAiService storeAiService;

	public StoreResponseDto createStore(StoreCreateRequestDto dto) {
		boolean exists = storeRepository.existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCase(
			dto.getName().trim(), dto.getAddress1().trim(), dto.getAddress2().trim()
		);
		if (exists) {
			throw new IllegalArgumentException("이미 등록된 가게입니다.");
		}
		String desc = storeAiService.generateAiDescription(dto.getName(), dto.getCategory());
		StoreEntity entity = StoreEntity.fromCreateDto(dto, desc);
		StoreEntity saved = storeRepository.save(entity);
		return toResponseDto(saved);
	}

	public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto dto) {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 가게가 없습니다."));

		store.updateFromDto(dto);
		String newDesc = storeAiService.generateAiDescription(dto.getName(), dto.getCategory());
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
			entity.getOwnerId(),
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
			entity.getIsAvailable()
		);
	}
}
