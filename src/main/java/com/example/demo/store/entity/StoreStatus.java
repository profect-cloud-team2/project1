package com.example.demo.store.entity;

import lombok.Getter;

@Getter
public enum StoreStatus {
	UNDER_REVIEW("등록심사중"),
	OPEN("영업중"),
	PREPARE("영업준비"),
	CLOSED_REQUESTED("폐업 신청"),
	CLOSED("폐업 완료"),
	DELETED("삭제됨");


	private final String description;

	StoreStatus(String description) {
		this.description = description;
	}

}
