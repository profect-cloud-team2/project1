package com.example.demo.store.dto;

import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StoreCreateRequestDto {

	@Schema(description = "가게 이름", example = "한솥도시락 강남점")
	@NotBlank
	private String name;

	@Schema(description = "사업자 등록번호", example = "123-45-67890")
	@NotBlank
	private String businessNum;

	@Schema(description = "카테고리", example = "KOREAN")
	@NotNull(message = "카테고리는 필수 입력 값입니다.")
	private Category category;

	@Schema(description = "도로명 주소", example = "서울시 강남구 테헤란로")
	@NotBlank
	private String address1;

	@Schema(description = "상세 주소", example = "101호")
	@NotBlank
	private String address2;

	@Schema(description = "전화번호", example = "01012345678")
	@NotBlank
	private String phoneNum;

	@Schema(description = "대표 이미지 URL", example = "https://example.com/image.jpg")
	private String imgURL;

	@Schema(description = "오픈 시간", example = "09:00")
	@NotBlank
	private String openTime;

	@Schema(description = "마감 시간", example = "21:00")
	@NotBlank
	private String closedTime;

	@Schema(description = "가게 위도", example = "37.4979")
	private BigDecimal storeLatitude;

	@Schema(description = "가게 경도", example = "127.0276")
	private BigDecimal storeLongitude;

	@Schema(description = "가게 소개", example = "든든한 한 끼를 제공하는 도시락 전문점입니다.")
	private String introduction;

	@Schema(description = "가게 운영 상태", example = "OPEN")
	@NotNull(message = "가게 운영 상태는 필수 입력 값입니다.")
	private StoreStatus isAvailable;
}
