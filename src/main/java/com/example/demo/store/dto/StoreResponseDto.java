package com.example.demo.store.dto;

import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StoreResponseDto {

	@Schema(description = "가게 고유 ID", example = "f1d2a9d2-1d7f-4eb7-9a1d-123456789abc")
	private UUID storeId;

	@Schema(description = "점주 ID")
	private UUID userId;

	@Schema(description = "가게 이름")
	private String name;

	@Schema(description = "사업자 등록번호")
	private String businessNum;

	@Schema(description = "카테고리")
	private Category category;

	@Schema(description = "도로명 주소")
	private String address1;

	@Schema(description = "상세 주소")
	private String address2;

	@Schema(description = "전화번호")
	private String phoneNum;

	@Schema(description = "이미지 URL")
	private String imgURL;

	@Schema(description = "오픈 시간", example = "09:00")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime openTime;

	@Schema(description = "마감 시간", example = "22:00")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime closedTime;

	@Schema(description = "AI 소개 문구")
	private String aiDescription;

	@Schema(description = "가게 소개 문구")
	private String introduction;

	@Schema(description = "가게 위도", example = "37.4979")
	private BigDecimal storeLatitude;

	@Schema(description = "가게 경도", example = "127.0276")
	private BigDecimal storeLongitude;

	@Schema(description = "가게 상태", example = "OPEN")
	private StoreStatus isAvailable;
}
