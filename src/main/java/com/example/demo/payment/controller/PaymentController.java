package com.example.demo.payment.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.DirectPaymentReq;
import com.example.demo.payment.dto.DirectPaymentRes;
import com.example.demo.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	//즉시결제
	@PostMapping("/direct")
	public ResponseEntity<DirectPaymentRes> directPay(@RequestBody DirectPaymentReq req) throws IOException {
		DirectPaymentRes response = paymentService.requestDirectPayment(req);
		return ResponseEntity.ok(response);
	}

	//인증결제
	@PostMapping("/ready")
	public ResponseEntity<CheckoutPaymentRes> ready(@RequestBody CheckoutPaymentReq req,
		@AuthenticationPrincipal String userIdStr) throws IOException {
		UUID userId = UUID.fromString(userIdStr);
		CheckoutPaymentRes response = paymentService.requestCheckoutPayment(req, userId);
		return ResponseEntity.ok(response);
	}

	//결제 취소
	@PostMapping("/{paymentKey}/cancel")
	public ResponseEntity<String> cancel(@RequestBody CancelPaymentReq req) throws IOException {
		CancelPaymentRes response = paymentService.requestCancelPayment(req);
		return ResponseEntity.ok("결제가 취소되었습니다.");
	}

	// 결제 성공
	@GetMapping("/payment/success")
	public ResponseEntity<String> paymentSuccess(
		@RequestParam String orderId,
		@RequestParam String paymentKey,
		@RequestParam int amount,
		@AuthenticationPrincipal String userIdStr) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			paymentService.confirmPaymentAndSaveOrder(paymentKey, UUID.fromString(orderId), amount, userId);
			return ResponseEntity.ok("결제가 성공 및 주문 저장 완료");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 처리 중 에러가 발생했습니다.");
		}
	}

	// 결제 실패
	@GetMapping("/fail")
	public ResponseEntity<String> fail() {
		return ResponseEntity.badRequest().body("결제에 실패했습니다.");
	}
}
