package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ConfirmPaymentRes {
	private String paymentKey;
	private UUID orderId;
	private String status;
	private int amount;
	private String approvedAt;
}
