package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CheckoutPaymentReq {
	private UUID storeId;
	private String orderName;
	private int amount;
	private String customerEmail;
	private String requestMessage;
}
