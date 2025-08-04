package com.example.demo.review.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewRequestDto {

	@NotNull
	private UUID storeId;

	@NotNull
	private UUID orderId;

	@Min(1)
	@Max(5)
	private Integer rating;

	private String content;
	private String imgURL;
}

