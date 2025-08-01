package com.example.demo.order.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.order.dto.StoreOrderResponseDto;
import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.service.OrderSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderSearchController {
	private final OrderSearchService orderSearchService;

	@GetMapping("/admin")
	public ResponseEntity<?> getAllOrders(@RequestParam(required = false) OrderStatus orderStatus, Pageable pageable) {
		Page<OrderEntity> orders = orderSearchService.getAllByStatus(orderStatus, pageable);
		return ResponseEntity.ok(orders);
	}

	// @GetMapping("/user/{userId}")
	// public ResponseEntity<?> getOrdersByUser(@PathVariable UUID userId, Pageable pageable) {
	// 	Page<OrderEntity> orders = orderSearchService.getOrderByUser(userId, pageable);
	// 	return ResponseEntity.ok(orders);
	// }

	@GetMapping("/store/{storeId}")
	public ResponseEntity<?> getOrdersByStore(@PathVariable UUID storeId, Pageable pageable) {
		Page<OrderEntity> orders = orderSearchService.getOrderByStore(storeId, pageable);
		return ResponseEntity.ok(new StoreOrderResponseDto<>("조회 완료", orders));
	}
}
