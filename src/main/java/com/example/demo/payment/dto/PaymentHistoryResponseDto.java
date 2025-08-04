package com.example.demo.payment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentHistoryResponseDto {
	private UUID paymentHistoryId;
	private UUID orderId;
	private String storeName;
	private String paymentMethod;
	private int totalAmount;
	private String status;
	private LocalDateTime approvedAt;
}