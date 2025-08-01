package com.example.demo.payment.service;

import java.io.IOException;
import java.util.UUID;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.ConfirmPaymentRes;
import com.example.demo.payment.dto.DirectPaymentReq;
import com.example.demo.payment.dto.DirectPaymentRes;

public interface PaymentService {
	DirectPaymentRes requestDirectPayment(DirectPaymentReq req) throws IOException;

	CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req) throws IOException;

	ConfirmPaymentRes confirmPayment(String paymentKey, UUID orderId, int amout) throws IOException;

	CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException;
}
