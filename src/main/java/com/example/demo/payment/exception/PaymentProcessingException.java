package com.example.demo.payment.exception;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException() {
        super("결제 처리 중 오류가 발생했습니다.");
    }
}