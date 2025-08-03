package com.example.demo.order.service;

import java.util.UUID;

import com.example.demo.order.dto.OrderCreateReq;

public interface OrderService {
	void createOrder(OrderCreateReq req, UUID userId);
	void cancelOrder(UUID orderId, UUID userId);
}
