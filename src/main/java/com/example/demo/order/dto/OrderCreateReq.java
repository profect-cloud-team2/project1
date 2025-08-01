package com.example.demo.order.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateReq {
	private UUID orderId;
	private String orderName;
	private int amount;
	private UUID storeId;
	private String requestMessage;
}
