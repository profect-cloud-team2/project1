package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class DirectPaymentReq {
	private UUID storeId;
	private String cardNumber;
	private String cardExpirationYear;
	private String cardExpirationMonth;
	private String cvc;
	private String customerIdentityNumber;

	private int amount;
	private UUID orderId;
	private String orderName;
}
