package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CheckoutPaymentRes {
	private UUID orderId;
	private String checkoutUrl;
}
