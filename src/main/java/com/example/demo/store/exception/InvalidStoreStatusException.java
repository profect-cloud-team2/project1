package com.example.demo.store.exception;

public class InvalidStoreStatusException extends RuntimeException {
	public InvalidStoreStatusException() {
		super("가게 상태는 OPEN 또는 PREPARE로만 변경할 수 있습니다.");
	}
}
