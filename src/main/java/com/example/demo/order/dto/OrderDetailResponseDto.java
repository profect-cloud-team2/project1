package com.example.demo.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.demo.order.entity.OrderStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailResponseDto {
	private UUID orderId;
	private String storeName;
	private int totalPrice;
	private OrderStatus orderStatus;
	private String requestMessage;
	private LocalDateTime createdAt;
	private List<OrderItemDto> orderItems;

	@Getter
	@Builder
	public static class OrderItemDto {
		private String menuName;
		private int quantity;
		private int price;
	}
}
