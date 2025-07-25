package com.example.demo.store.service;

import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreAiService storeAiService;

	public StoreEntity createStore(StoreEntity storeEntity) {
		boolean exists = storeRepository.existsByNameAndAddress1AndAddress2(
			storeEntity.getName(), storeEntity.getAddress1(), storeEntity.getAddress2()
		);
		if (exists) {
			throw new IllegalArgumentException("이미 등록된 가게입니다.");
		}

		String aiDesc = storeAiService.generateAiDescription(storeEntity.getName(), storeEntity.getCategory());
		storeEntity.setAiDescription(aiDesc);

		return storeRepository.save(storeEntity);
	}
}

