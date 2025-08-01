package com.example.demo.cart.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CartRes {
	private UUID cartId;
	private List<CartItemRes> items;
}
