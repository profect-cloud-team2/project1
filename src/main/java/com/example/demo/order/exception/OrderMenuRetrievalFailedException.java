package com.example.demo.order.exception;

public class OrderMenuRetrievalFailedException extends RuntimeException {
    public OrderMenuRetrievalFailedException() {
        super("주문 메뉴 조회 실패");
    }
}