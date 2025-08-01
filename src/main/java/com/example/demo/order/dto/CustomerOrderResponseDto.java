package com.example.demo.order.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.order.entity.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderResponseDto {
	private UUID orderId;
	private UUID userId;
	private UUID storeId;
	private String requestMessage;
	private OrderStatus orderStatus;
	private LocalDateTime createdAt;
}
