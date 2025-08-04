package com.example.demo.user.exception;

public class DuplicateLoginIdException extends RuntimeException {
	public DuplicateLoginIdException() {
		super("이미 존재하는 로그인 ID입니다.");
	}
}
