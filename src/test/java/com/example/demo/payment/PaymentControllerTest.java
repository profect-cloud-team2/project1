package com.example.demo.payment;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.payment.controller.PaymentController;
import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.PaymentReadyReq;
import com.example.demo.payment.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

	private MockMvc mockMvc;

	@Mock
	private PaymentService paymentService;

	@BeforeEach
	void setUp() {
		PaymentController paymentController = new PaymentController(paymentService);
		mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
	}

	@Test
	@DisplayName("결제 준비 성공")
	void ready_Success() throws Exception {
		String requestJson = "{\"customerEmail\":\"test@test.com\"}";

		CheckoutPaymentRes response = new CheckoutPaymentRes();
		response.setOrderId(UUID.randomUUID());
		response.setAmount(10000);

		given(paymentService.preparePaymentFromCart(any(PaymentReadyReq.class), any(UUID.class))).willReturn(response);

		mockMvc.perform(post("/api/payment/ready")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.param("userIdStr", UUID.randomUUID().toString()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("결제 취소 성공")
	void cancel_Success() throws Exception {
		String paymentKey = "test-payment-key";
		String requestJson = "{\"paymentKey\":\"test-payment-key\",\"cancelReason\":\"주문 취소\"}";

		CancelPaymentRes response = new CancelPaymentRes();
		response.setPaymentKey(paymentKey);

		given(paymentService.requestCancelPayment(any(CancelPaymentReq.class))).willReturn(response);

		mockMvc.perform(post("/api/payment/{paymentKey}/cancel", paymentKey)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("결제 성공 처리")
	void paymentSuccess_Success() throws Exception {
		String orderId = UUID.randomUUID().toString();
		String paymentKey = "test-payment-key";
		int amount = 10000;
		UUID userId = UUID.randomUUID();

		given(paymentService.getUserIdByOrderId(orderId)).willReturn(userId);
		doNothing().when(paymentService).confirmPaymentAndSaveOrder(paymentKey, orderId, amount, userId);

		mockMvc.perform(get("/api/payment/success")
				.param("orderId", orderId)
				.param("paymentKey", paymentKey)
				.param("amount", String.valueOf(amount)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("결제 실패 처리")
	void fail_Success() throws Exception {
		mockMvc.perform(get("/api/payment/fail"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("클라이언트 키 조회")
	void getClientKey_Success() throws Exception {
		mockMvc.perform(get("/api/payment/client-key"))
			.andExpect(status().isOk());
	}
}
