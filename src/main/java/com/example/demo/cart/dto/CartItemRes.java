package com.example.demo.cart.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CartItemRes {
	private UUID cartItemId;
	private UUID menuId;
	private String menuName;
	private int quantity;
	private int price;
}
