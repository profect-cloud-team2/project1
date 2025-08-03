package com.example.demo.payment.service;

import java.io.IOException;
import java.util.UUID;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.DirectPaymentReq;
import com.example.demo.payment.dto.DirectPaymentRes;

public interface PaymentService {
	DirectPaymentRes requestDirectPayment(DirectPaymentReq req) throws IOException;

	CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req, UUID userId) throws IOException;

	void confirmPaymentAndSaveOrder(String paymentKey, String orderId, int amount, UUID userId) throws IOException;
	
	void confirmPaymentAndSaveOrderForAnonymous(String paymentKey, String orderId, int amount) throws IOException;

	CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException;
}
