package com.example.demo.order.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderSearchService {

	private final OrderRepository orderRepository;

	public Page<OrderEntity> getAllByStatus(OrderStatus orderStatus, Pageable pageable) {
		Page<OrderEntity> orderEntityPage = orderRepository.findAllByOrderStatus(orderStatus, pageable);
		return orderEntityPage;
	}

	// public Page<OrderEntity> getOrderByUser(UUID userId, Pageable pageable) {
	// 	Page<OrderEntity> orderEntityPage = orderRepository.findByUser_UserId(userId, pageable);
	// 	return orderEntityPage;
	// }

	public Page<OrderEntity> getOrderByStore(UUID storeId, Pageable pageable) {
		return orderRepository.findByStoreId(storeId, pageable);
	}
}
