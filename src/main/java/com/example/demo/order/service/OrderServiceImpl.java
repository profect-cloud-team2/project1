package com.example.demo.order.service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.cart.service.CartServiceImpl;
import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.dto.OrderDetailResponseDto;
import com.example.demo.order.dto.OrderMenuResponseDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.entity.PaymentHistoryEntity;
import com.example.demo.payment.repository.PaymentHistoryRepository;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final CartServiceImpl cartService;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final PaymentService paymentService;
	private final PaymentHistoryRepository paymentHistoryRepository;

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

		if (!OrderStatus.RECEIVED.equals(order.getOrderStatus())) {
			throw new IllegalArgumentException("주문접수 상태에서만 취소 가능합니다.");
		}

		LocalDateTime orderTime = order.getCreatedAt();
		LocalDateTime now = LocalDateTime.now();
		if (orderTime.plusMinutes(10).isBefore(now)) {
			throw new IllegalArgumentException("주문 후 10분 이내에만 취소 가능합니다.");
		}

		// 결제 내역 조회 및 취소
		PaymentHistoryEntity paymentHistory = paymentHistoryRepository.findByOrderOrderIdAndDeletedAtIsNull(orderId)
			.orElse(null);

		if (paymentHistory != null) {
			try {
				CancelPaymentReq cancelReq = new CancelPaymentReq();
				cancelReq.setPaymentKey(paymentHistory.getPaymentKey());
				cancelReq.setCancelReason(OrderStatus.CANCELED.name());
				cancelReq.setCancelAmount(paymentHistory.getTotalAmount());
				paymentService.requestCancelPayment(cancelReq);

				// PaymentHistory 상태 업데이트
				PaymentHistoryEntity updatedHistory = PaymentHistoryEntity.builder()
					.paymentHistoryId(paymentHistory.getPaymentHistoryId())
					.order(paymentHistory.getOrder())
					.paymentKey(paymentHistory.getPaymentKey())
					.paymentMethod(paymentHistory.getPaymentMethod())
					.totalAmount(paymentHistory.getTotalAmount())
					.currency(paymentHistory.getCurrency())
					.status(OrderStatus.CANCELED.name())
					.requestedAt(paymentHistory.getRequestedAt())
					.approvedAt(paymentHistory.getApprovedAt())
					.createdAt(paymentHistory.getCreatedAt())
					.createdBy(paymentHistory.getCreatedBy())
					.updatedAt(LocalDateTime.now())
					.updatedBy(userId)
					.build();
			} catch (Exception e) {
				System.err.println("결제 취소 실패: " + e.getMessage());
				throw new IllegalStateException("결제 취소에 실패했습니다.");
			}
		}

		order.softDelete(userId);
		order.setOrderStatus(OrderStatus.CANCELED);
		orderRepository.save(order);
	}

	@Transactional
	public void autoCancel10MinuteOrders() {
		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

		orderRepository.findByOrderStatusAndCreatedAtBeforeAndDeletedAtIsNull(
				OrderStatus.RECEIVED, tenMinutesAgo)
			.forEach(order -> {
				// 결제 내역 조회 및 취소
				PaymentHistoryEntity paymentHistory = paymentHistoryRepository.findByOrderOrderIdAndDeletedAtIsNull(
						order.getOrderId())
					.orElse(null);

				if (paymentHistory != null) {
					try {
						CancelPaymentReq cancelReq = new CancelPaymentReq();
						cancelReq.setPaymentKey(paymentHistory.getPaymentKey());
						cancelReq.setCancelReason("자동 취소 (10분 초과)");
						cancelReq.setCancelAmount(paymentHistory.getTotalAmount());
						paymentService.requestCancelPayment(cancelReq);

						// PaymentHistory 상태 업데이트
						PaymentHistoryEntity updatedHistory = PaymentHistoryEntity.builder()
							.paymentHistoryId(paymentHistory.getPaymentHistoryId())
							.order(paymentHistory.getOrder())
							.paymentKey(paymentHistory.getPaymentKey())
							.paymentMethod(paymentHistory.getPaymentMethod())
							.totalAmount(paymentHistory.getTotalAmount())
							.currency(paymentHistory.getCurrency())
							.status(OrderStatus.CANCELED.name())
							.requestedAt(paymentHistory.getRequestedAt())
							.approvedAt(paymentHistory.getApprovedAt())
							.createdAt(paymentHistory.getCreatedAt())
							.createdBy(paymentHistory.getCreatedBy())
							.updatedAt(LocalDateTime.now())
							.updatedBy(order.getUser().getUserId())
							.build();
						paymentHistoryRepository.save(updatedHistory);
					} catch (Exception e) {
						System.err.println("자동 취소 시 결제 취소 실패: " + e.getMessage());
					}
				}

				order.softDelete(order.getUser().getUserId());
				order.setOrderStatus(OrderStatus.CANCELED);
				orderRepository.save(order);
				System.out.println("자동 취소된 주문: " + order.getOrderId());
			});
	}

	@Override
	@Transactional
	public void updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID ownerId) {
		OrderEntity order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

		if (!order.getStore().getUser().getUserId().equals(ownerId)) {
			throw new IllegalArgumentException("자신의 가게 주문만 상태 변경이 가능합니다.");
		}

		order.updateStatus(newStatus, ownerId);
		orderRepository.save(order);
	}

	@Override
	public Page<OrderResponseDto> getUserOrders(UUID userId, Pageable pageable) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		return orderRepository.findByUserUserIdAndDeletedAtIsNull(userId, pageable)
			.map(order -> OrderResponseDto.builder()
				.orderId(order.getOrderId())
				.storeName(order.getStore().getName())
				.totalPrice(order.getTotalPrice())
				.orderStatus(order.getOrderStatus())
				.requestMessage(order.getRequestMessage())
				.createdAt(order.getCreatedAt())
				.build());
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
				.totalPrice(order.getTotalPrice())
				.orderStatus(order.getOrderStatus())
				.requestMessage(order.getRequestMessage())
				.createdAt(order.getCreatedAt())
				.build());
	}

	@Override
	public Page<OrderMenuResponseDto> getMyOrdersWithMenu(UUID userId, Pageable pageable) {
		return orderRepository.findByUserUserIdAndDeletedAtIsNull(userId, pageable)
			.map(order -> OrderMenuResponseDto.builder()
				.orderId(order.getOrderId())
				.menuItems(order.getOrderItems().stream()
					.map(item -> OrderMenuResponseDto.MenuItemDto.builder()
						.menuName(item.getMenuName())
						.quantity(item.getQuantity())
						.price(item.getPrice())
						.build())
					.collect(Collectors.toList()))
				.build());
	}

	@Override
	public OrderDetailResponseDto getOrderDetail(UUID orderId, UUID userId) {
		OrderEntity order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

		if (!order.getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException("자신의 주문만 조회할 수 있습니다.");
		}

		return OrderDetailResponseDto.builder()
			.orderId(order.getOrderId())
			.storeName(order.getStore().getName())
			.totalPrice(order.getTotalPrice())
			.orderStatus(order.getOrderStatus())
			.requestMessage(order.getRequestMessage())
			.createdAt(order.getCreatedAt())
			.orderItems(order.getOrderItems().stream()
				.map(item -> OrderDetailResponseDto.OrderItemDto.builder()
					.menuName(item.getMenuName())
					.quantity(item.getQuantity())
					.price(item.getPrice())
					.build())
				.collect(Collectors.toList()))
			.build();
	}
}
