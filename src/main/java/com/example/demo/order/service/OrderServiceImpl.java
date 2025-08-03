package com.example.demo.order.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.cart.service.CartServiceImpl;
import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final CartServiceImpl cartService;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;

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

	@Override
	@Transactional
	public void cancelOrder(UUID orderId, UUID userId) {
		OrderEntity order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

		if (!order.getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException("자신의 주문만 취소할 수 있습니다.");
		}

		if (!OrderStatus.주문접수.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("주문접수 상태에서만 취소 가능합니다.");
		}

		LocalDateTime orderTime = order.getCreatedAt();
		LocalDateTime now = LocalDateTime.now();
		if (orderTime.plusMinutes(10).isBefore(now)) {
			throw new IllegalArgumentException("주문 후 10분 이내에만 취소 가능합니다.");
		}

		order.softDelete(userId);
		orderRepository.save(order);
	}

	@Transactional
	public void autoCancel10MinuteOrders() {
		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
		
		orderRepository.findByOrderStatusAndCreatedAtBeforeAndDeletedAtIsNull(
			OrderStatus.주문접수, tenMinutesAgo)
			.forEach(order -> {
				order.softDelete(order.getUser().getUserId());
				orderRepository.save(order);
				System.out.println("자동 취소된 주문: " + order.getOrderId());
			});
	}

	@Override
	@Transactional
	public void updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID ownerId) {
		OrderEntity order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

		// 가게 사장님만 상태 변경 가능
		if (!order.getStore().getUser().getUserId().equals(ownerId)) {
			throw new IllegalArgumentException("자신의 가게 주문만 상태 변경이 가능합니다.");
		}

		order.updateStatus(newStatus, ownerId);
		orderRepository.save(order);
	}

	@Override
	public Page<OrderResponseDto> getStoreOrders(UUID storeId, UUID ownerId, Pageable pageable) {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

		if (!store.getUser().getUserId().equals(ownerId)) {
			throw new IllegalArgumentException("자신의 가게 주문만 조회할 수 있습니다.");
		}

		return orderRepository.findByStoreIdAndDeletedAtIsNull(storeId, pageable)
			.map(order -> OrderResponseDto.builder()
				.orderId(order.getOrderId())
				.customerName(order.getUser().getName())
				.totalPrice(order.getTotalPrice())
				.orderStatus(order.getOrderStatus())
				.requestMessage(order.getRequestMessage())
				.createdAt(order.getCreatedAt())
				.build());
	}
}
