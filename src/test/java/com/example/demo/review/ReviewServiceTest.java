package com.example.demo.review;

import com.example.demo.order.repository.OrderRepository;
import com.example.demo.review.dto.ReviewRequestDto;
import com.example.demo.review.entity.ReviewEntity;
import com.example.demo.review.exception.ReviewAlreadyExistsException;
import com.example.demo.review.exception.ReviewInvalidOrderException;
import com.example.demo.review.exception.UnauthorizedOwnerReplyException;
import com.example.demo.review.repository.ReviewRepository;
import com.example.demo.review.service.ReviewService;
import com.example.demo.store.repository.StoreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private ReviewService reviewService;

	private UUID userId;
	private UUID orderId;
	private UUID storeId;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userId = UUID.randomUUID();
		orderId = UUID.randomUUID();
		storeId = UUID.randomUUID();
	}

	@Test
	void 리뷰_정상_작성() {
		System.out.println("✅ 리뷰 정상 작성 테스트 시작");

		// given
		ReviewRequestDto dto = ReviewRequestDto.builder()
			.orderId(orderId)
			.storeId(storeId)
			.rating(5)
			.content("최고예요")
			.imgURL("http://img.jpg")
			.build();

		when(orderRepository.existsByOrderIdAndUser_UserIdAndStore_StoreId(orderId, userId, storeId)).thenReturn(true);
		when(reviewRepository.existsByOrderIdAndDeletedAtIsNull(orderId)).thenReturn(false);

		ReviewEntity saved = ReviewEntity.builder()
			.reviewId(UUID.randomUUID())
			.orderId(orderId)
			.storeId(storeId)
			.userId(userId)
			.rating(5)
			.content("맛있어요")
			.build();

		when(reviewRepository.save(any())).thenReturn(saved);

		// when
		UUID result = reviewService.createReview(dto, userId);

		// then
		System.out.println("📌 생성된 리뷰 ID: " + result);
		assertThat(result).isNotNull();
		verify(reviewRepository, times(1)).save(any());
	}

	@Test
	void 리뷰_작성_실패_중복리뷰() {
		System.out.println("❌ 리뷰 작성 실패 - 중복 리뷰 테스트 시작");

		// given
		ReviewRequestDto dto = ReviewRequestDto.builder()
			.orderId(orderId)
			.storeId(storeId)
			.rating(4)
			.build();

		when(orderRepository.existsByOrderIdAndUser_UserIdAndStore_StoreId(orderId, userId, storeId)).thenReturn(true);
		when(reviewRepository.existsByOrderIdAndDeletedAtIsNull(orderId)).thenReturn(true);

		// when & then
		try {
			reviewService.createReview(dto, userId);
		} catch (ReviewAlreadyExistsException e) {
			System.out.println("예외 발생 메시지: " + e.getMessage());
			assertThat(e.getMessage()).isEqualTo("이미 이 주문에 대한 리뷰가 존재합니다.");
		}
	}

	@Test
	void 리뷰_작성_실패_본인주문아님() {
		System.out.println("❌ 리뷰 작성 실패 - 본인 주문 아님 테스트 시작");

		// given
		ReviewRequestDto dto = ReviewRequestDto.builder()
			.orderId(orderId)
			.storeId(storeId)
			.rating(3)
			.build();

		when(orderRepository.existsByOrderIdAndUser_UserIdAndStore_StoreId(orderId, userId, storeId)).thenReturn(false);

		// when & then
		try {
			reviewService.createReview(dto, userId);
			fail("예외가 발생하지 않았습니다."); // 예외가 없으면 실패
		} catch (ReviewInvalidOrderException e) {
			System.out.println("예외 발생 메시지: " + e.getMessage());
			assertThat(e.getMessage()).isEqualTo("해당 주문은 회원님의 주문이 아니거나, 존재하지 않습니다.");
		}
	}
	@Test
	void 사장님_리뷰_답글_정상작성() {
		System.out.println("✍️ 사장님 답글 작성 테스트 시작");

		// given
		UUID reviewId = UUID.randomUUID();
		String replyContent = "감사합니다!";
		UUID ownerId = UUID.randomUUID();
		UUID storeId = UUID.randomUUID();

		ReviewEntity review = ReviewEntity.builder()
			.reviewId(reviewId)
			.userId(UUID.randomUUID()) // 리뷰 작성자
			.storeId(storeId)
			.content("맛있어요")
			.build();

		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(storeRepository.existsByStoreIdAndUserUserId(storeId, ownerId)).thenReturn(true);

		// when
		reviewService.writeOwnerReply(reviewId, ownerId, replyContent);

		// then
		assertThat(review.getOwnerReview()).isEqualTo(replyContent);
		System.out.println("✅ 사장님 답글 내용: " + review.getOwnerReview());

		verify(reviewRepository).save(review);
	}
	@Test
	void 사장님_자기_가게_아닌_리뷰_답글_실패() {
		System.out.println("❌ 사장님 답글 권한 없는 가게 테스트");

		// given
		UUID reviewId = UUID.randomUUID();
		String replyContent = "감사합니다!";
		UUID ownerId = UUID.randomUUID();
		UUID storeId = UUID.randomUUID();

		ReviewEntity review = ReviewEntity.builder()
			.reviewId(reviewId)
			.userId(UUID.randomUUID())
			.storeId(storeId)
			.content("별로예요")
			.build();

		when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
		when(storeRepository.existsByStoreIdAndUserUserId(storeId, ownerId)).thenReturn(false);

		// when
		try {
			reviewService.writeOwnerReply(reviewId, ownerId, replyContent);
			fail("예외가 발생하지 않았습니다.");
		} catch (UnauthorizedOwnerReplyException e) {
			System.out.println("✅ 예외 메시지: " + e.getMessage());
			assertThat(e.getMessage()).isEqualTo("사장님 본인만 해당 리뷰에 답글을 작성/수정/삭제할 수 있습니다.");
		}
	}
}
