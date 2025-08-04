package com.example.demo.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
	RECEIVED("주문접수"),
	COOKING("조리중"),
	DELIVERING("배달중"),
	DELIVERD("배달완료"),
	CANCELED("주문취소");

	private final String description;

	OrderStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}

