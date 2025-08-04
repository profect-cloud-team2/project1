package com.example.demo.payment.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelPaymentReq {

	@NotNull
	private String paymentKey;

	@NotNull
	private String cancelReason;

	private int cancelAmount;

}
