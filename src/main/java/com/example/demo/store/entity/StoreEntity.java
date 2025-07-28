package com.example.demo.store.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID storeId;
	private UUID ownerId;

	private String name;
	private String businessNum;
	private String category;
	private String address1;
	private String address2;
	private String phoneNum;
	private String imgURL;
	private String openTime;
	private String closedTime;
	private String aiDescription;

	private String isAvailable = "ACTIVE";
	private boolean deleted = false;

	// 생성용
	public static StoreEntity fromCreateDto(com.example.demo.store.dto.StoreCreateRequestDto dto,
		String aiDescription) {
		StoreEntity entity = new StoreEntity();
		entity.name = dto.getName();
		entity.businessNum = dto.getBusinessNum();
		entity.ownerId = dto.getOwnerId();
		entity.category = dto.getCategory();
		entity.address1 = dto.getAddress1();
		entity.address2 = dto.getAddress2();
		entity.phoneNum = dto.getPhoneNum();
		entity.imgURL = dto.getImgURL();
		entity.openTime = dto.getOpenTime();
		entity.closedTime = dto.getClosedTime();
		entity.aiDescription = aiDescription;
		return entity;
	}

	// 수정용
	public void updateFromDto(com.example.demo.store.dto.StoreUpdateRequestDto dto) {
		this.name = dto.getName();
		this.category = dto.getCategory();
		this.address1 = dto.getAddress1();
		this.address2 = dto.getAddress2();
		this.phoneNum = dto.getPhoneNum();
		this.imgURL = dto.getImgURL();
		this.openTime = dto.getOpenTime();
		this.closedTime = dto.getClosedTime();
	}
}
