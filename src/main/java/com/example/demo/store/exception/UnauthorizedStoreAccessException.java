package com.example.demo.store.exception;

public class UnauthorizedStoreAccessException extends RuntimeException {
	public UnauthorizedStoreAccessException() {
		super("본인의 가게만 수정할 수 있습니다.");
	}
}
