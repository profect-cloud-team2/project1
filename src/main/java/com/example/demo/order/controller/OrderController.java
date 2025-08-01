package com.example.demo.order.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.order.dto.OrderCreateReq;
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
}
