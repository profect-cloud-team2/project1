package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class DirectPaymentRes {
	private String paymentKey;
	private UUID orderId;
	private int amount;
	private String method;
	private String cardCompany;
	private String approvedAt;
}
