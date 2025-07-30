package com.example.demo.payment.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.payment.client.TossPaymentClient;
import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.ConfirmPaymentRes;
import com.example.demo.payment.dto.DirectPaymentReq;
import com.example.demo.payment.dto.DirectPaymentRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final TossPaymentClient tossPaymentClient;

	@Override
	public DirectPaymentRes requestDirectPayment(DirectPaymentReq req) throws IOException {
		return tossPaymentClient.requestDirectPayment(req);
	}

	@Override
	public CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req) throws IOException {
		return tossPaymentClient.requestCheckoutPayment(req);
	}

	@Override
	public ConfirmPaymentRes confirmPayment(String paymentKey, UUID orderId, int amount) throws IOException {
		return tossPaymentClient.confirmPayment(paymentKey, orderId, amount);
	}

	@Override
	public CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException {
		return tossPaymentClient.requestCancelPayment(req);
	}
}
