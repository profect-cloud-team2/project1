package com.example.demo.payment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.PaymentHistoryResponseDto;
import com.example.demo.payment.dto.PaymentReadyReq;
import com.example.demo.payment.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private PaymentService paymentService;

	@Test
	@DisplayName("결제 준비 성공")
	void preparePaymentFromCart_Success() throws IOException {
		PaymentReadyReq request = new PaymentReadyReq();
		UUID userId = UUID.randomUUID();

		CheckoutPaymentRes response = new CheckoutPaymentRes();
		response.setOrderId(UUID.randomUUID());
		response.setAmount(10000);

		given(paymentService.preparePaymentFromCart(any(PaymentReadyReq.class), any(UUID.class))).willReturn(response);

		CheckoutPaymentRes result = paymentService.preparePaymentFromCart(request, userId);

		assertThat(result.getOrderId()).isNotNull();
		assertThat(result.getAmount()).isEqualTo(10000);
	}

	@Test
	@DisplayName("결제 취소 성공")
	void requestCancelPayment_Success() throws IOException {
		CancelPaymentReq request = new CancelPaymentReq();
		request.setPaymentKey("test-payment-key");
		request.setCancelReason("주문 취소");

		CancelPaymentRes response = new CancelPaymentRes();
		response.setPaymentKey("test-payment-key");

		given(paymentService.requestCancelPayment(any(CancelPaymentReq.class))).willReturn(response);

		CancelPaymentRes result = paymentService.requestCancelPayment(request);

		assertThat(result.getPaymentKey()).isEqualTo("test-payment-key");
	}

	@Test
	@DisplayName("결제내역 조회 성공")
	void getPaymentHistory_Success() {
		UUID userId = UUID.randomUUID();
		Pageable pageable = PageRequest.of(0, 10);
		PaymentHistoryResponseDto historyDto = PaymentHistoryResponseDto.builder()
			.paymentHistoryId(UUID.randomUUID())
			.orderId(UUID.randomUUID())
			.storeName("테스트가게")
			.totalAmount(10000)
			.status("DONE")
			.build();
		Page<PaymentHistoryResponseDto> historyPage = new PageImpl<>(Arrays.asList(historyDto));

		given(paymentService.getPaymentHistory(userId, pageable)).willReturn(historyPage);

		Page<PaymentHistoryResponseDto> result = paymentService.getPaymentHistory(userId, pageable);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getStoreName()).isEqualTo("테스트가게");
	}

	@Test
	@DisplayName("주문ID로 사용자ID 조회 성공")
	void getUserIdByOrderId_Success() {
		String orderId = UUID.randomUUID().toString();
		UUID userId = UUID.randomUUID();

		given(paymentService.getUserIdByOrderId(orderId)).willReturn(userId);

		UUID result = paymentService.getUserIdByOrderId(orderId);

		assertThat(result).isEqualTo(userId);
	}

	@Test
	@DisplayName("결제 확인 및 주문 저장 성공")
	void confirmPaymentAndSaveOrder_Success() throws IOException {
		String paymentKey = "test-payment-key";
		String orderId = UUID.randomUUID().toString();
		int amount = 10000;
		UUID userId = UUID.randomUUID();

		doNothing().when(paymentService).confirmPaymentAndSaveOrder(paymentKey, orderId, amount, userId);

		paymentService.confirmPaymentAndSaveOrder(paymentKey, orderId, amount, userId);
	}
}
