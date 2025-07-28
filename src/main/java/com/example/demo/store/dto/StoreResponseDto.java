package com.example.demo.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreResponseDto {
	private String name;
	private String category;
	private String address;
	private String aiDescription;
}
