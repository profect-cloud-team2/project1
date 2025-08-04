package com.example.demo.review.repository;

import com.example.demo.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
	boolean existsByOrderIdAndDeletedAtIsNull(UUID orderId);

	List<ReviewEntity> findByStoreIdAndDeletedAtIsNull(UUID storeId);
	List<ReviewEntity> findByUserIdAndDeletedAtIsNull(UUID userId);
}
