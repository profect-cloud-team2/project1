package com.example.demo.review.service;

import com.example.demo.order.repository.OrderRepository;
import com.example.demo.review.dto.ReviewRequestDto;
import com.example.demo.review.dto.ReviewResponseDto;
import com.example.demo.review.entity.ReviewEntity;
import com.example.demo.review.exception.ReviewAlreadyExistsException;
import com.example.demo.review.exception.ReviewInvalidOrderException;
import com.example.demo.review.exception.ReviewNotFoundException;
import com.example.demo.review.exception.UnauthorizedOwnerReplyException;
import com.example.demo.review.repository.ReviewRepository;
import com.example.demo.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final StoreRepository storeRepository;
	private final OrderRepository orderRepository;

	public UUID createReview(ReviewRequestDto dto, UUID userId) {
		boolean isValidOrder = orderRepository.existsByOrderIdAndUser_UserIdAndStore_StoreId(
			dto.getOrderId(), userId, dto.getStoreId()
		);
		if (!isValidOrder) {
			throw new ReviewInvalidOrderException();
		}

		if (reviewRepository.existsByOrderIdAndDeletedAtIsNull(dto.getOrderId())) {
			throw new ReviewAlreadyExistsException();
		}

		ReviewEntity entity = ReviewEntity.builder()
			.userId(userId)
			.storeId(dto.getStoreId())
			.orderId(dto.getOrderId())
			.rating(dto.getRating())
			.content(dto.getContent())
			.imgURL(dto.getImgURL())
			.createdBy(userId)
			.build();

		return reviewRepository.save(entity).getReviewId();
	}

	public List<ReviewResponseDto> getReviewsByStore(UUID storeId) {
		return reviewRepository.findByStoreIdAndDeletedAtIsNull(storeId).stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	public List<ReviewResponseDto> getReviewsByUser(UUID userId) {
		List<ReviewEntity> reviews = reviewRepository.findByUserIdAndDeletedAtIsNull(userId);

		if (reviews.isEmpty()) {
			throw new ReviewNotFoundException();
		}

		return reviews.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	public ReviewEntity getReviewById(UUID reviewId) {
		return reviewRepository.findById(reviewId)
			.orElseThrow(ReviewNotFoundException::new);
	}

	public void deleteReview(UUID reviewId, UUID deletedBy) {
		ReviewEntity review = getReviewById(reviewId);
		review.setDeletedAt(LocalDateTime.now());
		review.setDeletedBy(deletedBy);
		reviewRepository.save(review);
	}

	public void writeOwnerReply(UUID reviewId, UUID ownerId, String replyContent) {
		ReviewEntity review = getReviewById(reviewId);

		if (!storeRepository.existsByStoreIdAndUserUserId(review.getStoreId(), ownerId)) {
			throw new UnauthorizedOwnerReplyException();
		}

		review.setOwnerReview(replyContent);
		review.setUpdatedBy(ownerId);
		review.setUpdatedAt(LocalDateTime.now());
		reviewRepository.save(review);
	}
	public void deleteOwnerReply(UUID reviewId, UUID ownerId) {
		ReviewEntity review = getReviewById(reviewId);

		if (!storeRepository.existsByStoreIdAndUserUserId(review.getStoreId(), ownerId)) {
			throw new UnauthorizedOwnerReplyException();
		}

		review.setOwnerReview(null);
		review.setUpdatedBy(ownerId);
		review.setUpdatedAt(LocalDateTime.now());
		reviewRepository.save(review);
	}

	private ReviewResponseDto toDto(ReviewEntity r) {
		return ReviewResponseDto.builder()
			.reviewId(r.getReviewId())
			.userId(r.getUserId())
			.storeId(r.getStoreId())
			.rating(r.getRating())
			.content(r.getContent())
			.imgURL(r.getImgURL())
			.ownerReview(r.getOwnerReview())
			.createdAt(r.getCreatedAt())
			.build();
	}
}
