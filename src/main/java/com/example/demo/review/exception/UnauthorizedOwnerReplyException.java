package com.example.demo.review.exception;

public class UnauthorizedOwnerReplyException extends RuntimeException {
	public UnauthorizedOwnerReplyException() {
		super("사장님 본인만 해당 리뷰에 답글을 작성/수정/삭제할 수 있습니다.");
	}
}
