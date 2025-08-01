package com.example.demo.search.dto;

import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
	private UUID storeId;
	private String storeName;
	private String category;
	private String imgURL;

	public static SearchResultDto fromEntity(StoreEntity store) {
		return new SearchResultDto(
			store.getStoreId(),
			store.getName(),
			store.getCategory().getDescription(),
			store.getImgURL()
		);
	}
}
