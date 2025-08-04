package com.example.demo.payment.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.PaymentHistoryResponseDto;
import com.example.demo.payment.dto.PaymentReadyReq;

public interface PaymentService {

	CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req, UUID userId) throws IOException;

	CheckoutPaymentRes preparePaymentFromCart(PaymentReadyReq req, UUID userId) throws IOException;

	void confirmPaymentAndSaveOrder(String paymentKey, String orderId, int amount, UUID userId) throws IOException;

	CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException;

	UUID getUserIdByOrderId(String orderId);

	Page<PaymentHistoryResponseDto> getPaymentHistory(UUID userId, Pageable pageable);
}
