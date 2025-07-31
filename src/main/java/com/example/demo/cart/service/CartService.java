package com.example.demo.cart.service;

import java.util.List;
import java.util.UUID;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.dto.CartRes;

public interface CartService {

	List<CartRes> getMyCart();

	void addItemToCart(CartItemAddReq req);

	void updateQuantity(UUID cartItemId, int quantity);

	void deleteItem(UUID cartItemId);
}
