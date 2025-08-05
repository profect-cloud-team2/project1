package com.example.demo.payment.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.PaymentHistoryResponseDto;
import com.example.demo.payment.dto.PaymentReadyReq;
import com.example.demo.payment.exception.PaymentProcessingException;
import com.example.demo.payment.exception.UnauthorizedException;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@Value("${payment.toss.client-key}")
	private String clientKey;

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@PostMapping("/ready")
	public ResponseEntity<CheckoutPaymentRes> ready(@RequestBody PaymentReadyReq req,
		@AuthenticationPrincipal UserEntity user) throws IOException {
		if (user == null) {
			throw new UnauthorizedException();
		}
		CheckoutPaymentRes response = paymentService.preparePaymentFromCart(req, user.getUserId());
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@PostMapping("/{paymentKey}/cancel")
	public ResponseEntity<String> cancel(@RequestBody CancelPaymentReq req) throws IOException {
		paymentService.requestCancelPayment(req);
		return ResponseEntity.ok("결제가 취소되었습니다.");
	}

	@GetMapping("/success")
	public ResponseEntity<String> paymentSuccess(
		@RequestParam String orderId,
		@RequestParam String paymentKey,
		@RequestParam int amount) {

		String cleanOrderId = orderId.contains(",") ? orderId.split(",")[0] : orderId;

		try {
			UUID user = paymentService.getUserIdByOrderId(cleanOrderId);
			if (user == null) {
				throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다.");
			}
			paymentService.confirmPaymentAndSaveOrder(paymentKey, cleanOrderId, amount, user);
			return ResponseEntity.ok("결제가 성공 및 주문 저장 완료");
		} catch (Exception e) {
			throw new PaymentProcessingException();
		}
	}

	@GetMapping("/fail")
	public ResponseEntity<String> fail() {
		return ResponseEntity.badRequest().body("결제에 실패했습니다.");
	}

	@GetMapping("/client-key")
	public ResponseEntity<String> getClientKey() {
		return ResponseEntity.ok(clientKey);
	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@GetMapping("/history")
	public ResponseEntity<Page<PaymentHistoryResponseDto>> getPaymentHistory(@AuthenticationPrincipal UserEntity user,
		Pageable pageable) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		Page<PaymentHistoryResponseDto> history = paymentService.getPaymentHistory(user.getUserId(), pageable);
		return ResponseEntity.ok(history);
	}
}
