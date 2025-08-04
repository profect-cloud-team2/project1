package com.example.demo.review.exception;

public class ReviewAlreadyExistsException extends RuntimeException {
	public ReviewAlreadyExistsException() {
		super("이미 이 주문에 대한 리뷰가 존재합니다.");
	}
}
