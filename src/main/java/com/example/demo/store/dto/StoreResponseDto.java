package com.example.demo.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class StoreResponseDto {

	@Schema(description = "가게 고유 ID", example = "f1d2a9d2-1d7f-4eb7-9a1d-123456789abc")
	private UUID storeId;

	@Schema(description = "점주 ID")
	private UUID ownerId;

	@Schema(description = "가게 이름")
	private String name;

	@Schema(description = "사업자 등록번호")
	private String businessNum;

	@Schema(description = "카테고리")
	private String category;

	@Schema(description = "도로명 주소")
	private String address1;

	@Schema(description = "상세 주소")
	private String address2;

	@Schema(description = "전화번호")
	private String phoneNum;

	@Schema(description = "이미지 URL")
	private String imgURL;

	@Schema(description = "오픈 시간")
	private String openTime;

	@Schema(description = "마감 시간")
	private String closedTime;

	@Schema(description = "AI 소개 문구")
	private String aiDescription;

	@Schema(description = "가게 상태", example = "ACTIVE")
	private String isAvailable;
}
