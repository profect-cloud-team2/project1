package com.example.demo.payment.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.ConfirmPaymentRes;
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
	public ResponseEntity<CheckoutPaymentRes> ready(@RequestBody CheckoutPaymentReq req) throws IOException {
		CheckoutPaymentRes response = paymentService.requestCheckoutPayment(req);
		return ResponseEntity.ok(response);
	}

	// 결제 성공
	@GetMapping("/success")
	public ResponseEntity<ConfirmPaymentRes> confirmPayment(
		@RequestParam String paymentKey,
		@RequestParam UUID orderId,
		@RequestParam int amount) throws IOException {

		ConfirmPaymentRes result = paymentService.confirmPayment(paymentKey, orderId, amount);
		return ResponseEntity.ok(result);
	}

	// 결제 실패
	@GetMapping("/fail")
	public ResponseEntity<String> fail() {
		return ResponseEntity.badRequest().body("결제에 실패했습니다.");
	}
}
