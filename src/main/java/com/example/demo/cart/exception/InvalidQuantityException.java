package com.example.demo.cart.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("잘못된 수량입니다.");
    }
}