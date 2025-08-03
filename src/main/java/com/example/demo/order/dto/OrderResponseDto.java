package com.example.demo.order.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.order.entity.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponseDto {
	private UUID orderId;
	private String customerName;
	private int totalPrice;
	private OrderStatus orderStatus;
	private String requestMessage;
	private LocalDateTime createdAt;
}