package com.example.demo.cart.dto;

import java.util.List;
import java.util.UUID;

import com.example.demo.cart.entity.CartItemEntity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemAddReq {

	@NotNull
	private UUID storeId;

	@NotEmpty
	private List<CartItemEntity> MenuItems;

	public static class MenuItem {
		@NotNull
		private UUID menuId;

		@Min(1)
		private int quantity;

		@Min(0)
		private int price;
	}
}
