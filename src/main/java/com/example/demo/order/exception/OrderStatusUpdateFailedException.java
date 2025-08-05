package com.example.demo.order.exception;

public class OrderStatusUpdateFailedException extends RuntimeException {
    public OrderStatusUpdateFailedException() {
        super("주문 상태 변경 실패");
    }
}