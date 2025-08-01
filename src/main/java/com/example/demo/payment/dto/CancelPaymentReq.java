package com.example.demo.payment.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Data;

@Data
public class CancelPaymentReq {

	@NotNull
	private String paymentKey;

	@NotNull
	private String cancelReason;

	private int cancelAmount;

}
