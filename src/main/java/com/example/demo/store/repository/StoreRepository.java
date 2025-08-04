package com.example.demo.store.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.store.entity.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {
	boolean existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCaseAndDeletedAtIsNull(
		String name, String address1, String address2
	);

	/**
	 * storeId가 userId(사장)의 가게로 등록되어 있는지 여부
	 */
	boolean existsByStoreIdAndUserUserId(
		UUID storeId, UUID userId
	);

	Optional<StoreEntity> findByStoreIdAndDeletedAtIsNull(UUID StoreId);

}



