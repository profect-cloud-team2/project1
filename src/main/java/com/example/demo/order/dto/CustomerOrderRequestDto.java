package com.example.demo.order.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderRequestDto {
	private UUID userId;
	private UUID storeId;
	private List<UUID> menuItemId;
	private int totalPrice;
	private String requestMessage;
}
