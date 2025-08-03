package com.example.demo.payment.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${payment.toss.client-key}")
	private String clientKey;

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

		if (userIdStr == null) {
			return ResponseEntity.status(401).body(null);
		}

		UUID userId = UUID.fromString(userIdStr);

		req.setSuccessUrl("http://localhost:8080/api/payment/success");
		req.setFailUrl("http://localhost:8080/api/payment/fail");

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
	@GetMapping("/success")
	public ResponseEntity<String> paymentSuccess(
		@RequestParam(required = false) String userId,
		@RequestParam String orderId,
		@RequestParam String paymentKey,
		@RequestParam int amount) {

		String cleanOrderId = orderId.contains(",") ? orderId.split(",")[0] : orderId;

		try {
			UUID user = paymentService.getUserIdByOrderId(cleanOrderId);
			if (user == null) {
				return ResponseEntity.badRequest().body("주문 정보를 찾을 수 없습니다.");
			}

			paymentService.confirmPaymentAndSaveOrder(paymentKey, cleanOrderId, amount, user);
			return ResponseEntity.ok("결제가 성공 및 주문 저장 완료");
		} catch (Exception e) {
			System.err.println("결제 처리 오류: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("결제 처리 중 에러가 발생했습니다: " + e.getMessage());
		}
	}

	// 결제 실패
	@GetMapping("/fail")
	public ResponseEntity<String> fail() {
		return ResponseEntity.badRequest().body("결제에 실패했습니다.");
	}

	// 클라이언트 키 조회
	@GetMapping("/client-key")
	public ResponseEntity<String> getClientKey() {
		return ResponseEntity.ok(clientKey);
	}
}
