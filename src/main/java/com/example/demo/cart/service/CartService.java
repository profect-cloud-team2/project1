package com.example.demo.cart.service;

import java.util.List;
import java.util.UUID;

import com.example.demo.cart.dto.CartAddReq;
import com.example.demo.cart.dto.CartAddRes;
import com.example.demo.cart.dto.CartRes;

public interface CartService {
	CartAddRes addCart(CartAddReq req);

	List<CartRes> getMyCarts();

	void deleteCart(UUID cartId);
}
