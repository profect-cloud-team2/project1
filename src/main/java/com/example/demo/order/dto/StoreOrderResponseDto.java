package com.example.demo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreOrderResponseDto<T> {
	private String message;
	private T data;
}
