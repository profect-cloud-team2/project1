package com.example.demo.payment.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutPaymentReq {
	private String orderName;
	private int amount;
	private String customerEmail;
	private String successUrl;
	private String failUrl;
	
	// 서버에서 설정되는 필드
	private UUID orderId;
}
