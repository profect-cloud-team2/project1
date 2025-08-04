package com.example.demo.review.repository;

import com.example.demo.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
	@Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.storeId = :storeId AND r.deletedAt IS NULL")
	Optional<Double> calculateAverageRatingByStoreId(@Param("storeId") UUID storeId);

	boolean existsByOrderIdAndDeletedAtIsNull(UUID orderId);

	List<ReviewEntity> findByStoreIdAndDeletedAtIsNull(UUID storeId);
	List<ReviewEntity> findByUserIdAndDeletedAtIsNull(UUID userId);
}
