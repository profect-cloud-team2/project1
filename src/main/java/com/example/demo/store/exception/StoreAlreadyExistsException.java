package com.example.demo.store.exception;

public class StoreAlreadyExistsException extends RuntimeException {
	public StoreAlreadyExistsException() {
		super("이미 등록된 가게입니다.");
	}
}
