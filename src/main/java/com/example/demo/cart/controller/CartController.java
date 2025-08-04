package com.example.demo.cart.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.demo.cart.exception.UnauthorizedException;
import com.example.demo.cart.service.CartService;
import com.example.demo.user.entity.UserEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<String> addItems(@RequestBody @Valid CartItemAddReq req,
		@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		cartService.addItemToCart(req, user.getUserId());
		return ResponseEntity.ok("장바구니에 추가되었습니다.");
	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<CartRes>> getMyCart(@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		List<CartRes> myCart = cartService.getMyCart(user.getUserId());
		return ResponseEntity.ok(myCart);
	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@PatchMapping("/items/{cartItemId}")
	public ResponseEntity<String> updateQuantity(@PathVariable UUID cartItemId, @RequestParam int quantity,
		@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		cartService.updateQuantity(cartItemId, quantity, user.getUserId());
		return ResponseEntity.ok("수량이 변경되었습니다.");
	}

	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	@DeleteMapping("/items/{cartItemId}")
	public ResponseEntity<String> deleteItem(@PathVariable UUID cartItemId, @AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		cartService.deleteItem(cartItemId, user.getUserId());
		return ResponseEntity.ok("장바구니 항목이 삭제되었습니다.");
	}
}
