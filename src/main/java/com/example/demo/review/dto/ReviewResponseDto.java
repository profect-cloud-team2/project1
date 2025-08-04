package com.example.demo.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ReviewResponseDto {
	private UUID reviewId;
	private UUID userId;
	private UUID storeId;
	private int rating;
	private String content;
	private String imgURL;
	private String ownerReview;
	private LocalDateTime createdAt;
}
