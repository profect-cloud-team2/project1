package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CancelPaymentRes {
	private String paymentKey;
	private UUID orderId;
	private String orderName;
	private String status;
	private String requestedAt;
	private String approvedAt;
}
