package com.example.demo.user.exception;

public class DuplicateNicknameException extends RuntimeException {
	public DuplicateNicknameException() {
		super("이미 존재하는 닉네임입니다.");
	}
}
