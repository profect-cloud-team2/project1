package com.example.demo.order.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderMenuResponseDto {
	private UUID orderId;
	private List<MenuItemDto> menuItems;
	
	@Getter
	@Builder
	public static class MenuItemDto {
		private String menuName;
		private int quantity;
		private int price;
	}
}