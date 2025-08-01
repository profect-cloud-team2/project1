package com.example.demo.admin.exception;

public class ReportAlreadyDeletedException extends RuntimeException {
	public ReportAlreadyDeletedException() {
		super("이미 삭제된 신고입니다.");
	}
}
