package com.example.demo.order.exception;

public class OrderDetailRetrievalFailedException extends RuntimeException {
    public OrderDetailRetrievalFailedException() {
        super("주문 상세 조회 실패");
    }
}