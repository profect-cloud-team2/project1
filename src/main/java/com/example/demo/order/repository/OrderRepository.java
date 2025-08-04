package com.example.demo.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
	boolean existsByOrderIdAndUser_UserIdAndStore_StoreId(UUID orderId, UUID userId, UUID storeId);

	Page<OrderEntity> findAllByOrderStatusAndDeletedAtIsNull(OrderStatus orderStatus, Pageable pageable);

	@Query("SELECT o FROM OrderEntity o WHERE o.store.storeId = :storeId AND o.deletedAt IS NULL ORDER BY o.createdAt DESC")
	Page<OrderEntity> findByStoreIdAndDeletedAtIsNull(UUID storeId, Pageable pageable);

	List<OrderEntity> findByOrderStatusAndCreatedAtBeforeAndDeletedAtIsNull(
		OrderStatus orderStatus, LocalDateTime createdAt);

	@Query("SELECT o FROM OrderEntity o WHERE o.orderId = :orderId AND o.deletedAt IS NULL")
	Optional<OrderEntity> findByIdAndDeletedAtIsNull(UUID orderId);

	@Query("SELECT o FROM OrderEntity o WHERE o.user.userId = :userId AND o.deletedAt IS NULL ORDER BY o.createdAt DESC")
	Page<OrderEntity> findByUserUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);
}
