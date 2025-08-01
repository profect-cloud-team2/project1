package com.example.demo.order.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public void createOrder(OrderCreateReq req, UUID userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		StoreEntity store = storeRepository.findById(req.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다"));

		OrderEntity order = OrderEntity.builder()
			.orderId(req.getOrderId())
			.userId(user)
			.storeId(store)
			.totalPrice(req.getAmount())
			.requestMessage(req.getRequestMessage())
			.orderStatus(OrderStatus.주문접수)
			.createdAt(LocalDateTime.now())
			.createdBy(userId)
			.build();

		orderRepository.save(order);
	}
}
