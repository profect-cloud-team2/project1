package com.example.demo.store.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {

	boolean existsByNameIgnoreCaseAndAddress1IgnoreCaseAndAddress2IgnoreCaseAndDeletedAtIsNull(
		String name, String address1, String address2
	);

	@Query("SELECT DISTINCT s FROM StoreEntity s " +
		"LEFT JOIN s.menus m " +
		"WHERE s.deletedAt IS NULL AND s.isAvailable <> com.example.demo.store.entity.StoreStatus.DELETED " +
		"AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		"OR LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		"OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<StoreEntity> searchVisibleStoresByKeyword(@Param("keyword") String keyword, Pageable pageable);
	Page<StoreEntity> findAllByIsAvailable(StoreStatus status, Pageable pageable);

	boolean existsByStoreIdAndUserUserId(
		UUID storeId, UUID userId
	);

	Optional<StoreEntity> findByStoreIdAndDeletedAtIsNull(UUID StoreId);

}



