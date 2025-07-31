package com.example.demo.cart.service;

import com.example.demo.cart.dto.CartAddReq;
import com.example.demo.cart.dto.CartAddRes;

public interface CartService {
	CartAddRes addCart(CartAddReq req);
}
