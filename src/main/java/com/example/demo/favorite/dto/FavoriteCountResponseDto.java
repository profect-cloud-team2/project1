package com.example.demo.favorite.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class FavoriteCountResponseDto {
	private UUID storeId;
	private long count;

	// public String getStoreId(){
	// 	return storeId;
	// }
	//
	// public long getCount(){
	// 	return count;
	// }
}
