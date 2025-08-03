package com.example.demo.order.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
	Page<OrderEntity> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

	// 가게별 주문 페이지 조회
	@Query("SELECT o FROM OrderEntity o WHERE o.store.storeId = :storeId")
	Page<OrderEntity> findByStoreId(UUID storeId, Pageable pageable);
}
