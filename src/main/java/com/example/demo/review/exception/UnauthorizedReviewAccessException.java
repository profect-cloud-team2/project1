package com.example.demo.review.exception;

public class UnauthorizedReviewAccessException extends RuntimeException {
	public UnauthorizedReviewAccessException() {
		super("본인의 리뷰만 삭제할 수 있습니다.");
	}
}


