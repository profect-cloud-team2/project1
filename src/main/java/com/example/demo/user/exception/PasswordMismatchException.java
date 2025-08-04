package com.example.demo.user.exception;

public class PasswordMismatchException extends RuntimeException {
	public PasswordMismatchException() {
		super("변경할 비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
	}
}
