package com.example.demo.order.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.order.service.OrderServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

	private final OrderServiceImpl orderService;

	// 매 1분마다 10분 초과 주문 자동 취소
	@Scheduled(fixedRate = 60000)
	public void autoCancel10MinuteOrders() {
		orderService.autoCancel10MinuteOrders();
	}
}