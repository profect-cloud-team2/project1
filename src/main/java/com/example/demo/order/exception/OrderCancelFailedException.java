package com.example.demo.order.exception;

public class OrderCancelFailedException extends RuntimeException {
    public OrderCancelFailedException() {
        super("주문 취소 실패");
    }
}