package com.example.demo.admin.exception;

public class ReportNotFoundException extends RuntimeException {
	public ReportNotFoundException() {
		super("신고 내역이 존재하지 않습니다.");
	}
}
