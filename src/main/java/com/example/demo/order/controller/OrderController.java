package com.example.demo.order.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.dto.OrderDetailResponseDto;
import com.example.demo.order.dto.OrderMenuResponseDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.dto.OrderStatusUpdateReq;
import com.example.demo.order.exception.UnauthorizedException;
import com.example.demo.order.service.OrderService;
import com.example.demo.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/create")
	public ResponseEntity<String> creatOrder(@RequestBody OrderCreateReq req,
		@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		orderService.createOrder(req, user.getUserId());
		return ResponseEntity.ok().body("주문이 성공적으로 저장되었습니다.");
	}

	@GetMapping("/store/{storeId}")
	public ResponseEntity<Page<OrderResponseDto>> getStoreOrders(@PathVariable UUID storeId,
		@AuthenticationPrincipal UserEntity user,
		Pageable pageable) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		Page<OrderResponseDto> orders = orderService.getStoreOrders(storeId, user.getUserId(), pageable);
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/my")
	public ResponseEntity<Page<OrderResponseDto>> getUserOrders(@AuthenticationPrincipal UserEntity user,
		Pageable pageable) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		Page<OrderResponseDto> orders = orderService.getUserOrders(user.getUserId(), pageable);
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/my/detail")
	public ResponseEntity<Page<OrderMenuResponseDto>> getMyOrdersWithMenu(@AuthenticationPrincipal UserEntity user,
		Pageable pageable) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		Page<OrderMenuResponseDto> orders = orderService.getMyOrdersWithMenu(user.getUserId(), pageable);
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDetailResponseDto> getOrderDetail(@PathVariable UUID orderId,
		@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		OrderDetailResponseDto order = orderService.getOrderDetail(orderId, user.getUserId());
		return ResponseEntity.ok(order);
	}

	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId,
		@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		orderService.cancelOrder(orderId, user.getUserId());
		return ResponseEntity.ok("주문이 취소되었습니다.");
	}

	@PatchMapping("/{orderId}/status")
	public ResponseEntity<String> updateOrderStatus(@PathVariable UUID orderId,
		@RequestBody OrderStatusUpdateReq req,
		@AuthenticationPrincipal UserEntity user) {
		if (user == null) {
			throw new UnauthorizedException();
		}
		orderService.updateOrderStatus(orderId, req.getOrderStatus(), user.getUserId());
		return ResponseEntity.ok("주문 상태가 변경되었습니다.");
	}
}