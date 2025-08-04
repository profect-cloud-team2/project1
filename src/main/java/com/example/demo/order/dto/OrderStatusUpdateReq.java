package com.example.demo.order.dto;

import com.example.demo.order.entity.OrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateReq {
	private OrderStatus orderStatus;
}