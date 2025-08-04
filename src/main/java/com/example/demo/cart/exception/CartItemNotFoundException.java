package com.example.demo.cart.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException() {
        super("장바구니 아이템을 찾을 수 없습니다.");
    }
}