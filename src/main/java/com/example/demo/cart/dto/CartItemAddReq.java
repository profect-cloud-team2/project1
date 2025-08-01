package com.example.demo.cart.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemAddReq {
	
	private UUID storeId;

	@NotEmpty
	private List<MenuItem> MenuItems;

	@Data
	public static class MenuItem {
		@NotNull
		private UUID menuId;

		@Min(1)
		private int quantity;

		@Min(0)
		private int price;
	}
}
