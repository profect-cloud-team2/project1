package com.example.demo.cart.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.dto.CartRes;
import com.example.demo.cart.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<String> addItems(@RequestBody @Valid CartItemAddReq req) {
		cartService.addItemToCart(req);
		return ResponseEntity.ok("장바구니에 추가되었습니다.");
	}

	@GetMapping
	public ResponseEntity<List<CartRes>> getMyCart() {
		List<CartRes> myCart = cartService.getMyCart();
		return ResponseEntity.ok(myCart);
	}

	@PatchMapping("/items/{cartItemId}")
	public ResponseEntity<String> updateQuantity(@PathVariable UUID cartItemId, @RequestParam int quantity) {
		cartService.updateQuantity(cartItemId, quantity);
		return ResponseEntity.ok("수량이 변경되었습니다.");
	}

	@DeleteMapping("/items/{cartItemId}")
	public ResponseEntity<String> deleteItem(@PathVariable UUID cartItemId) {
		cartService.deleteItem(cartItemId);
		return ResponseEntity.ok("장바구니 항목이 삭제되었습니다.");
	}
}
