package com.example.demo.store.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.store.entity.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {
	boolean existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCase(
		String name, String address1, String address2
	);

	StoreEntity findByStoreId(UUID storeId);
}


