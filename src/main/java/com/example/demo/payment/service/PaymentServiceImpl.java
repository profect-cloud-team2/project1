package com.example.demo.payment.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.entity.CartItemEntity;
import com.example.demo.cart.repository.CartItemRepository;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderItemEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderItemRepository;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.payment.client.TossPaymentClient;
import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.ConfirmPaymentRes;
import com.example.demo.payment.dto.PaymentHistoryResponseDto;
import com.example.demo.payment.dto.PaymentReadyReq;
import com.example.demo.payment.entity.PaymentHistoryEntity;
import com.example.demo.payment.repository.PaymentHistoryRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final TossPaymentClient tossPaymentClient;
	private final Map<String, UUID> orderUserMap = new ConcurrentHashMap<>();
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final PaymentHistoryRepository paymentHistoryRepository;

	@Override
	public CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req, UUID userId) throws IOException {
		UUID orderId = UUID.randomUUID();

		req.setOrderId(orderId);
		orderUserMap.put(orderId.toString(), userId);

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("유저 없음"));
		return tossPaymentClient.requestCheckoutPayment(req);
	}

	@Override
	public void confirmPaymentAndSaveOrder(String paymentKey, String orderId, int amount, UUID userId) throws
		IOException {
		if (paymentKey.startsWith("tviva")) {
			System.out.println("테스트 결제 - 결제 확인 건너뛰기");
		} else {
			ConfirmPaymentRes res = tossPaymentClient.confirmPayment(paymentKey, UUID.fromString(orderId), amount);
			if (!"DONE".equals(res.getStatus())) {
				throw new IllegalStateException("결제 완료 상태가 아닙니다");
			}
		}

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));

		CartEntity cart = cartRepository.findWithItemsByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("장바구니 없음"));

		List<CartItemEntity> cartItems = cart.getItems();

		if (cartItems.isEmpty()) {
			throw new IllegalStateException("장바구니에 담긴 항목이 없습니다");
		}

		UUID storeId = cartItems.get(0).getMenu().getStore().getStoreId();

		OrderEntity order = OrderEntity.builder()
			.orderId(UUID.fromString(orderId))
			.user(user)
			.store(cartItems.get(0).getMenu().getStore())
			.totalPrice(amount) // 테스트에서는 전달받은 amount 사용
			.orderStatus(OrderStatus.RECEIVED)
			.requestMessage("요청사항 없음")
			.createdAt(LocalDateTime.now())
			.createdBy(user.getUserId())
			.build();

		orderRepository.save(order);

		// 장바구니 아이템들을 주문 아이템으로 저장
		cartItems.forEach(cartItem -> {
			OrderItemEntity orderItem = OrderItemEntity.builder()
				.order(order)
				.menu(cartItem.getMenu())
				.storeName(cartItem.getMenu().getStore().getName())
				.menuName(cartItem.getMenu().getName())
				.quantity(cartItem.getQuantity())
				.price(cartItem.getMenu().getPrice())
				.requestMessage("요청사항 없음")
				.createdAt(LocalDateTime.now())
				.createdBy(user.getUserId())
				.build();
			orderItemRepository.save(orderItem);
		});

		// PaymentHistory 저장
		PaymentHistoryEntity paymentHistory = PaymentHistoryEntity.builder()
			.paymentHistoryId(UUID.randomUUID())
			.order(order)
			.paymentKey(paymentKey)
			.paymentMethod("CARD")
			.totalAmount(amount)
			.currency("KRW")
			.status("DONE")
			.requestedAt(LocalDateTime.now())
			.approvedAt(LocalDateTime.now())
			.createdAt(LocalDateTime.now())
			.createdBy(user.getUserId())
			.build();
		paymentHistoryRepository.save(paymentHistory);

		// 주문 완료 후 장바구니 hardDelete
		cartItemRepository.deleteAll(cartItems);
		cartRepository.delete(cart);
	}

	@Override
	public CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException {
		return tossPaymentClient.requestCancelPayment(req);
	}

	@Override
	public CheckoutPaymentRes preparePaymentFromCart(PaymentReadyReq req, UUID userId) throws IOException {
		// 장바구니에서 결제 정보 계산
		CartEntity cart = cartRepository.findWithItemsByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("장바구니가 비어있습니다."));

		List<CartItemEntity> cartItems = cart.getItems();
		if (cartItems.isEmpty()) {
			throw new IllegalArgumentException("장바구니에 상품이 없습니다.");
		}

		// 총 금액 계산
		int totalAmount = cartItems.stream()
			.mapToInt(item -> item.getMenu().getPrice() * item.getQuantity())
			.sum();

		// 주문명 생성
		String orderName = cartItems.get(0).getMenu().getName();
		if (cartItems.size() > 1) {
			orderName += " 외 " + (cartItems.size() - 1) + "건";
		}

		// CheckoutPaymentReq 생성
		CheckoutPaymentReq checkoutReq = new CheckoutPaymentReq();
		checkoutReq.setAmount(totalAmount);
		checkoutReq.setOrderName(orderName);
		checkoutReq.setCustomerEmail(req.getCustomerEmail());
		checkoutReq.setSuccessUrl("http://localhost:8080/api/payment/success");
		checkoutReq.setFailUrl("http://localhost:8080/api/payment/fail");

		return requestCheckoutPayment(checkoutReq, userId);
	}

	@Override
	public UUID getUserIdByOrderId(String orderId) {
		return orderUserMap.get(orderId);
	}

	@Override
	public Page<PaymentHistoryResponseDto> getPaymentHistory(UUID userId, Pageable pageable) {
		return paymentHistoryRepository.findByUserIdAndDeletedAtIsNull(userId, pageable)
			.map(payment -> PaymentHistoryResponseDto.builder()
				.paymentHistoryId(payment.getPaymentHistoryId())
				.orderId(payment.getOrder().getOrderId())
				.storeName(payment.getOrder().getStore().getName())
				.paymentMethod(payment.getPaymentMethod())
				.totalAmount(payment.getTotalAmount())
				.status(payment.getStatus())
				.approvedAt(payment.getApprovedAt())
				.build());
	}
}
