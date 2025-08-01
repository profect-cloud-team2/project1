package com.example.demo.store.entity;

import lombok.Getter;

@Getter
public enum Category {
	KOREAN("한식"),
	CHINESE("중식"),
	WESTERN("양식"),
	JAPANESE("일식"),
	LATENIGHT("야식");

	private final String description;

	Category(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
