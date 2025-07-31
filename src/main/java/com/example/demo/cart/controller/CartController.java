package com.example.demo.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.cart.dto.CartAddReq;
import com.example.demo.cart.dto.CartAddRes;
import com.example.demo.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<CartAddRes> addCart(@RequestBody CartAddReq req) {
		CartAddRes response = cartService.addCart(req);
		return ResponseEntity.ok(response);
	}
}
