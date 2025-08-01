package com.example.demo.payment.dto;

import lombok.Data;

@Data
public class CheckoutPaymentReq {
	private int amount;
	private String orderId;
	private String orderName;
	private String customerEmail;
}
