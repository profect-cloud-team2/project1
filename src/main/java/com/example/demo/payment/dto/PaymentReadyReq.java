package com.example.demo.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentReadyReq {
	private String customerEmail;
}