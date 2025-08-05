package com.example.demo.store.exception;

public class StoreNotFoundException extends RuntimeException {
	public StoreNotFoundException() {
		super("해당 가게가 없습니다.");
	}
}
