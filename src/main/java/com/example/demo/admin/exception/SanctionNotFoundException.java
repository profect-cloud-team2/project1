package com.example.demo.admin.exception;

public class SanctionNotFoundException extends RuntimeException {
	public SanctionNotFoundException() {
		super("해당 제재가 존재하지 않습니다.");
	}
}
