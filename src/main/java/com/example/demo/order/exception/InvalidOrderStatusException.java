package com.example.demo.order.exception;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException() {
        super("잘못된 주문 상태입니다.");
    }
}