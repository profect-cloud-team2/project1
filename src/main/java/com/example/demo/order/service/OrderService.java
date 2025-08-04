package com.example.demo.order.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.dto.OrderDetailResponseDto;
import com.example.demo.order.dto.OrderMenuResponseDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.entity.OrderStatus;

public interface OrderService {
	void createOrder(OrderCreateReq req, UUID userId);

	void cancelOrder(UUID orderId, UUID userId);

	void updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID ownerId);

	Page<OrderResponseDto> getStoreOrders(UUID storeId, UUID ownerId, Pageable pageable);

	Page<OrderResponseDto> getUserOrders(UUID userId, Pageable pageable);

	Page<OrderMenuResponseDto> getMyOrdersWithMenu(UUID userId, Pageable pageable);

	OrderDetailResponseDto getOrderDetail(UUID orderId, UUID userId);
}
