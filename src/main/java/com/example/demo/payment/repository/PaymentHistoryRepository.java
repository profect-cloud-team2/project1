package com.example.demo.payment.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.payment.entity.PaymentHistoryEntity;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistoryEntity, UUID> {
	
	@Query("SELECT p FROM PaymentHistoryEntity p WHERE p.order.user.userId = :userId AND p.deletedAt IS NULL")
	Page<PaymentHistoryEntity> findByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);
	
	Optional<PaymentHistoryEntity> findByOrderOrderIdAndDeletedAtIsNull(UUID orderId);
}