package com.example.demo.order.exception;

public class OrderCreateFailedException extends RuntimeException {
    public OrderCreateFailedException() {
        super("주문 저장 실패");
    }
}