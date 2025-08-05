package com.example.demo.order.exception;

public class OrderRetrievalFailedException extends RuntimeException {
    public OrderRetrievalFailedException() {
        super("주문 조회 실패");
    }
}