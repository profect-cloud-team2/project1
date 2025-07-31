package com.example.demo.cart.service;

import com.example.demo.cart.dto.CartItemAddReq;

public interface CartItemService {
	void addItemsToCart(CartItemAddReq req);
}
