package com.example.demo.admin.entity;

import lombok.Getter;

@Getter
public enum SanctionStatus {
	WARNING("경고"),
	SUSPEND("일시 정지"),
	BAN("영구 정지");

	private final String displayName;

	SanctionStatus(String displayName) {
		this.displayName = displayName;
	}
}
