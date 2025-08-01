package com.example.demo.store.repository;

import java.util.UUID;

import com.example.demo.store.entity.StoreEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {
	boolean existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCase(
		String name, String address1, String address2
	);

	/**
	 * storeId가 userId(사장)의 가게로 등록되어 있는지 여부
	 */
	boolean existsByStoreIdAndUserUserId(
		UUID storeId, UUID userId
	);
}


