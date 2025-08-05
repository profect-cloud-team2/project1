package com.example.demo.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// public class AiResult {
// }
public class StoreAiResponseDto {
	private final String request;
	private final String response;
}
