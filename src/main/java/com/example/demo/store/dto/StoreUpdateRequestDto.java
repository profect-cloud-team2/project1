package com.example.demo.store.dto;

import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class StoreUpdateRequestDto {

	@Schema(description = "가게 이름", example = "수정된 한솥도시락 강남점")
	@NotBlank
	private String name;

	@Schema(description = "카테고리", example = "한식")
	@NotBlank
	private Category category;

	@Schema(description = "도로명 주소", example = "서울시 강남구 역삼로")
	@NotBlank
	private String address1;

	@Schema(description = "상세 주소", example = "202호")
	@NotBlank
	private String address2;

	@Schema(description = "전화번호", example = "01098765432")
	@NotBlank
	private String phoneNum;

	@Schema(description = "대표 이미지 URL", example = "https://example.com/newimage.jpg")
	private String imgURL;

	@Schema(description = "오픈 시간", example = "10:00")
	@NotBlank
	private String openTime;

	@Schema(description = "마감 시간", example = "22:00")
	@NotBlank
	private String closedTime;

	@Schema(description = "가게 위도", example = "37.4979")
	private BigDecimal storeLatitude;

	@Schema(description = "가게 경도", example = "127.0276")
	private BigDecimal storeLongitude;

	@Schema(description = "가게 소개 문구", example = "든든한 한 끼를 제공하는 도시락 전문점입니다.")
	private String introduction;

	@Schema(description = "가게 상태", example = "OPEN")
	private StoreStatus isAvailable;
}
