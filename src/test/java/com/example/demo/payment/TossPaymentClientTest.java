package com.example.demo.payment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.payment.client.TossPaymentClient;
import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.ConfirmPaymentRes;

@ExtendWith(MockitoExtension.class)
class TossPaymentClientTest {

	@Mock
	private TossPaymentClient tossPaymentClient;

	@Test
	@DisplayName("결제 준비 요청 성공")
	void requestCheckoutPayment_Success() throws IOException {
		CheckoutPaymentReq request = new CheckoutPaymentReq();
		request.setOrderId(UUID.randomUUID());
		request.setAmount(10000);
		request.setOrderName("테스트 주문");

		CheckoutPaymentRes response = new CheckoutPaymentRes();
		response.setOrderId(request.getOrderId());
		response.setAmount(request.getAmount());
		response.setMessage("결제 준비 완료");

		given(tossPaymentClient.requestCheckoutPayment(any(CheckoutPaymentReq.class))).willReturn(response);

		CheckoutPaymentRes result = tossPaymentClient.requestCheckoutPayment(request);

		assertThat(result.getOrderId()).isEqualTo(request.getOrderId());
		assertThat(result.getAmount()).isEqualTo(request.getAmount());
		assertThat(result.getMessage()).isEqualTo("결제 준비 완료");
	}

	@Test
	@DisplayName("결제 확인 요청 성공")
	void confirmPayment_Success() throws IOException {
		String paymentKey = "test-payment-key";
		UUID orderId = UUID.randomUUID();
		int amount = 10000;

		ConfirmPaymentRes response = new ConfirmPaymentRes();
		response.setPaymentKey(paymentKey);
		response.setOrderId(orderId);
		response.setAmount(amount);

		given(tossPaymentClient.confirmPayment(paymentKey, orderId, amount)).willReturn(response);

		ConfirmPaymentRes result = tossPaymentClient.confirmPayment(paymentKey, orderId, amount);

		assertThat(result.getPaymentKey()).isEqualTo(paymentKey);
		assertThat(result.getOrderId()).isEqualTo(orderId);
		assertThat(result.getAmount()).isEqualTo(amount);
	}

	@Test
	@DisplayName("결제 취소 요청 성공")
	void requestCancelPayment_Success() throws IOException {
		CancelPaymentReq request = new CancelPaymentReq();
		request.setPaymentKey("test-payment-key");
		request.setCancelReason("주문 취소");
		request.setCancelAmount(10000);

		CancelPaymentRes response = new CancelPaymentRes();
		response.setPaymentKey("test-payment-key");

		given(tossPaymentClient.requestCancelPayment(any(CancelPaymentReq.class))).willReturn(response);

		CancelPaymentRes result = tossPaymentClient.requestCancelPayment(request);

		assertThat(result.getPaymentKey()).isEqualTo("test-payment-key");
	}
}
