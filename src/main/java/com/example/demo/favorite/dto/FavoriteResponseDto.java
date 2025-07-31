package com.example.demo.favorite.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteResponseDto {
	private UUID storeId;
	private String storeName;
}
