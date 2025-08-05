package com.example.demo.user.exception;

public class InvalidUserEditRequestException extends RuntimeException {
	public InvalidUserEditRequestException() {
		super("변경하실 정보를 입력해주세요.");
	}
}
