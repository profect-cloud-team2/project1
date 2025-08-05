package com.example.demo.order.exception;

public class UnauthorizedOrderAccessException extends RuntimeException {
    public UnauthorizedOrderAccessException() {
        super("주문에 접근할 권한이 없습니다.");
    }
}