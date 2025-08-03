package com.example.demo.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.cart.service.CartServiceImpl;
import com.example.demo.order.dto.OrderCreateReq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final CartServiceImpl cartService;

	@Override
	@Transactional
	public void createOrder(OrderCreateReq req, UUID userId) {
		UUID orderId = cartService.createOrderFromCart(
			req.getCartItemIds(),
			req.getStoreId(),
			req.getRequestMessage(),
			userId
		);

		System.out.println("주문 생성 완료: " + orderId);
	}
}
