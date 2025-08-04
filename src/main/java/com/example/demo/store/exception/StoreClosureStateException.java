package com.example.demo.store.exception;

public class StoreClosureStateException extends RuntimeException {
	public StoreClosureStateException() {
		super("이미 폐업 요청 중이거나 폐업된 가게입니다.");
	}
}
