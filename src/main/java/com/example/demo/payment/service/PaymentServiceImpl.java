// package com.example.demo.payment.service;
//
// import java.io.IOException;
// import java.util.UUID;
//
// import org.springframework.stereotype.Service;
//
// import com.example.demo.order.entity.OrderEntity;
// import com.example.demo.payment.client.TossPaymentClient;
// import com.example.demo.payment.dto.CancelPaymentReq;
// import com.example.demo.payment.dto.CancelPaymentRes;
// import com.example.demo.payment.dto.CheckoutPaymentReq;
// import com.example.demo.payment.dto.CheckoutPaymentRes;
// import com.example.demo.payment.dto.ConfirmPaymentRes;
// import com.example.demo.payment.dto.DirectPaymentReq;
// import com.example.demo.payment.dto.DirectPaymentRes;
// import com.example.demo.user.entity.UserEntity;
// import com.example.demo.user.repository.UserRepository;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class PaymentServiceImpl implements PaymentService {
//
// 	private final TossPaymentClient tossPaymentClient;
// 	private final UserRepository userRepository;
//
// 	@Override
// 	public DirectPaymentRes requestDirectPayment(DirectPaymentReq req) throws IOException {
// 		return tossPaymentClient.requestDirectPayment(req);
// 	}
//
// 	@Override
// 	public CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req, UUID userId) throws IOException {
// 		UUID orderId = UUID.randomUUID();
//
// 		UserEntity user = userRepository.findById(userId)
// 			.orElseThrow(() -> new IllegalArgumentException("유저 없음"));
// 		return tossPaymentClient.requestCheckoutPayment(req);
// 	}
//
// 	@Override
// 	public ConfirmPaymentRes confirmPayment(String paymentKey, String orderId, int amount) throws IOException {
// 		ConfirmPaymentRes res = tossPaymentClient.confirmPayment(paymentKey, UUID.fromString(orderId), amount);
//
// 		if (!"DONE".equals(res.getStatus())) {
// 			throw new IllegalStateException("결제 완료 상태가 아닙니다");
// 		}
//
// 		OrderEntity order = OrderEntity.builder()
// 			.orderId(UUID.fromString(orderId))
// 			.userId(user)
//
// 		)
// 	}
//
// 	@Override
// 	public CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException {
// 		return tossPaymentClient.requestCancelPayment(req);
// 	}
// }
