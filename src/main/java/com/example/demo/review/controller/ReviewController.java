package com.example.demo.review.controller;

import com.example.demo.review.dto.OwnerReplyRequestDto;
import com.example.demo.review.dto.ReviewRequestDto;
import com.example.demo.review.dto.ReviewResponseDto;
import com.example.demo.review.entity.ReviewEntity;
import com.example.demo.review.exception.UnauthorizedOwnerReplyException;
import com.example.demo.review.exception.UnauthorizedReviewAccessException;
import com.example.demo.review.service.ReviewService;
import com.example.demo.user.entity.UserEntity;

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

	@PostMapping
	public UUID createReview(@RequestBody @Valid ReviewRequestDto dto,
		@AuthenticationPrincipal UserEntity user) {
		return reviewService.createReview(dto, user.getUserId());
	}

	@GetMapping("/store/{storeId}")
	public List<ReviewResponseDto> getReviews(@PathVariable UUID storeId) {
		return reviewService.getReviewsByStore(storeId);
	}

	@GetMapping("/my")
	public List<ReviewResponseDto> getMyReviews(@AuthenticationPrincipal UserEntity user) {
		UUID userId = user.getUserId();
		return reviewService.getReviewsByUser(userId);
	}

	@PatchMapping("/{reviewId}/owner-reply")
	public void writeOrUpdateOwnerReply(
		@PathVariable UUID reviewId,
		@RequestBody OwnerReplyRequestDto request,
		@AuthenticationPrincipal UserEntity user
	) {
		validateOwner(user);
		reviewService.writeOwnerReply(reviewId, user.getUserId(), request.getReplyContent());
	}

	@DeleteMapping("/{reviewId}/owner-reply")
	public void deleteOwnerReply(
		@PathVariable UUID reviewId,
		@AuthenticationPrincipal UserEntity user
	) {
		validateOwner(user);
		reviewService.deleteOwnerReply(reviewId, user.getUserId());
	}

	@DeleteMapping("/{reviewId}")
	public void deleteReview(@PathVariable UUID reviewId,
		@AuthenticationPrincipal UserEntity user) {
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
