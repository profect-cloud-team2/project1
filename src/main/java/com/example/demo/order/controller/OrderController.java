package com.example.demo.order.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.dto.OrderStatusUpdateReq;
import com.example.demo.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/create")
	public ResponseEntity<String> creatOrder(@RequestBody OrderCreateReq req,
		@AuthenticationPrincipal String userIdStr) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			orderService.createOrder(req, userId);
			return ResponseEntity.ok().body("주문이 성공적으로 저장되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 저장 실패" + e.getMessage());
		}
	}

	@GetMapping("/store/{storeId}")
	public ResponseEntity<?> getStoreOrders(@PathVariable UUID storeId,
		@AuthenticationPrincipal String userIdStr,
		Pageable pageable) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			Page<OrderResponseDto> orders = orderService.getStoreOrders(storeId, userId, pageable);
			return ResponseEntity.ok(orders);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 조회 실패: " + e.getMessage());
		}
	}

	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId,
		@AuthenticationPrincipal String userIdStr) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			orderService.cancelOrder(orderId, userId);
			return ResponseEntity.ok("주문이 취소되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 취소 실패: " + e.getMessage());
		}
	}

	@PatchMapping("/{orderId}/status")
	public ResponseEntity<String> updateOrderStatus(@PathVariable UUID orderId,
		@RequestBody OrderStatusUpdateReq req,
		@AuthenticationPrincipal String userIdStr) {
		try {
			UUID userId = UUID.fromString(userIdStr);
			orderService.updateOrderStatus(orderId, req.getOrderStatus(), userId);
			return ResponseEntity.ok("주문 상태가 변경되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 상태 변경 실패: " + e.getMessage());
		}
	}
}
