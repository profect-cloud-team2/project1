package com.example.demo.cart.service;

import java.util.List;
import java.util.UUID;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.dto.CartRes;

public interface CartService {

	List<CartRes> getMyCart(UUID userId);

	void addItemToCart(CartItemAddReq req, UUID userId);

	void updateQuantity(UUID cartItemId, int quantity, UUID userId);

	void deleteItem(UUID cartItemId, UUID userId);
}
