package com.example.demo.store.service;

import java.util.UUID;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreAiService storeAiService;
	private final UserRepository userRepository;

	public StoreResponseDto createStore(StoreCreateRequestDto dto, String userId) {
		boolean exists = storeRepository.existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCase(
			dto.getName().trim(), dto.getAddress1().trim(), dto.getAddress2().trim()
		);
		if (exists) {
			throw new IllegalArgumentException("이미 등록된 가게입니다.");
		}

		String desc = storeAiService.generateAiDescription(
			dto.getName(),
			dto.getCategory()
		);

		UserEntity user = userRepository.findById(UUID.fromString(userId))
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		StoreEntity entity = StoreEntity.fromCreateDto(dto, user, desc);
		entity.setCreatedBy(user.getUserId());
		StoreEntity saved = storeRepository.save(entity);

		return toResponseDto(saved);
	}


	public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto dto) {
		StoreEntity store = storeRepository.findById(storeId)
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
}
