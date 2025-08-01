package com.example.demo.admin.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportStatus {
	PENDING("처리 대기"),
	DONE("처리 완료"),
	REJECTED("거절됨");

	private final String description;

	ReportStatus(String description) {
		this.description = description;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}
}
