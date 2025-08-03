package com.example.demo.review.exception;

public class ReviewInvalidOrderException extends RuntimeException {
	public ReviewInvalidOrderException() {
		super("해당 주문은 회원님의 주문이 아니거나, 존재하지 않습니다.");
	}
}
