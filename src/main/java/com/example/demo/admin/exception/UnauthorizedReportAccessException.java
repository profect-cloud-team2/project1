package com.example.demo.admin.exception;

public class UnauthorizedReportAccessException extends RuntimeException {
	public UnauthorizedReportAccessException() {
		super("관리자만 신고를 삭제할 수 있습니다.");
	}
}
