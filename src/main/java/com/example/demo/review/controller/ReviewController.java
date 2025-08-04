package com.example.demo.review.controller;

import com.example.demo.review.dto.OwnerReplyRequestDto;
import com.example.demo.review.dto.ReviewRequestDto;
import com.example.demo.review.dto.ReviewResponseDto;
import com.example.demo.review.entity.ReviewEntity;
import com.example.demo.review.exception.UnauthorizedOwnerReplyException;
import com.example.demo.review.exception.UnauthorizedReviewAccessException;
import com.example.demo.review.service.ReviewService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	private void validateOwner(UserEntity user) {
		if (user.getRole() != UserEntity.UserRole.OWNER) {
			throw new UnauthorizedOwnerReplyException();
		}
	}

	@Operation(
		summary = "리뷰 작성",
		description = "유저가 리뷰를 작성합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 입력"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		},
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			content = @Content(
				schema = @Schema(implementation = ReviewRequestDto.class),
				examples = @ExampleObject(
					value = """
					{
					  "storeId": "d3a4d95e-d4b5-4b2f-a93d-1c9d8c234eb7",
					  "rating": 5,
					  "content": "맛있어요!"
					}
					"""
				)
			)
		)
	)
	@PostMapping
	public UUID createReview(
		@Valid @RequestBody ReviewRequestDto dto,
		@AuthenticationPrincipal UserEntity user
	) {
		return reviewService.createReview(dto, user.getUserId());
	}

	@Operation(
		summary = "가게 리뷰 목록 조회",
		description = "특정 가게의 리뷰를 전체 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공"),
			@ApiResponse(responseCode = "404", description = "해당 가게를 찾을 수 없음")
		}
	)
	@GetMapping("/store/{storeId}")
	public List<ReviewResponseDto> getReviews(
		@Parameter(description = "가게 ID") @PathVariable UUID storeId
	) {
		return reviewService.getReviewsByStore(storeId);
	}

	@Operation(
		summary = "내가 작성한 리뷰 목록 조회",
		description = "내가 작성한 리뷰들을 전체 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "내 리뷰 목록 조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
		}
	)
	@GetMapping("/my")
	public List<ReviewResponseDto> getMyReviews(@AuthenticationPrincipal UserEntity user) {
		return reviewService.getReviewsByUser(user.getUserId());
	}

	@Operation(
		summary = "사장님 답글 작성/수정",
		description = "사장님이 특정 리뷰에 대한 답글을 작성하거나 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "답글 작성 또는 수정 성공"),
			@ApiResponse(responseCode = "403", description = "사장님 권한이 아님"),
			@ApiResponse(responseCode = "404", description = "해당 리뷰 없음")
		},
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			content = @Content(
				schema = @Schema(implementation = OwnerReplyRequestDto.class),
				examples = @ExampleObject(
					value = """
					{
					  "replyContent": "방문해주셔서 감사합니다!"
					}
					"""
				)
			)
		)
	)
	@PatchMapping("/{reviewId}/owner-reply")
	public void writeOrUpdateOwnerReply(
		@Parameter(description = "리뷰 ID") @PathVariable UUID reviewId,
		@RequestBody OwnerReplyRequestDto request,
		@AuthenticationPrincipal UserEntity user
	) {
		validateOwner(user);
		reviewService.writeOwnerReply(reviewId, user.getUserId(), request.getReplyContent());
	}

	@Operation(
		summary = "사장님 답글 삭제",
		description = "사장님이 리뷰에 남긴 답글을 삭제처리합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "답글 삭제 성공"),
			@ApiResponse(responseCode = "403", description = "사장님 권한이 아님"),
			@ApiResponse(responseCode = "404", description = "해당 리뷰 없음")
		}
	)
	@PatchMapping("/{reviewId}/owner-reply/delete")
	public void deleteOwnerReply(
		@Parameter(description = "리뷰 ID") @PathVariable UUID reviewId,
		@AuthenticationPrincipal UserEntity user
	) {
		validateOwner(user);
		reviewService.deleteOwnerReply(reviewId, user.getUserId());
	}

	@Operation(
		summary = "리뷰 삭제",
		description = "본인 또는 관리자 권한으로 리뷰를 삭제처리합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
			@ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
			@ApiResponse(responseCode = "404", description = "해당 리뷰 없음")
		}
	)
	@PatchMapping("/{reviewId}")
	public void deleteReview(
		@Parameter(description = "리뷰 ID") @PathVariable UUID reviewId,
		@AuthenticationPrincipal UserEntity user
	) {
		if (user.getRole() == UserEntity.UserRole.ADMIN) {
			reviewService.deleteReview(reviewId, user.getUserId());
			return;
		}

		ReviewEntity review = reviewService.getReviewById(reviewId);
		if (!review.getUserId().equals(user.getUserId())) {
			throw new UnauthorizedReviewAccessException();
		}
		reviewService.deleteReview(reviewId, user.getUserId());
	}
}
