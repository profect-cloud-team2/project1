package com.example.demo.review;

import com.example.demo.global.exception.GlobalExceptionHandler;
import com.example.demo.review.controller.ReviewController;
import com.example.demo.review.dto.OwnerReplyRequestDto;
import com.example.demo.review.service.ReviewService;
import com.example.demo.user.entity.UserEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ReviewService reviewService;

	@InjectMocks
	private ReviewController reviewController;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
			.setControllerAdvice(new GlobalExceptionHandler()) // 전역 예외 처리기 연결
			.build();
	}

	@Test
	void HTTP_PATCH_사장이_아닌_유저가_답글_작성시_403_예외() throws Exception {
		UUID reviewId = UUID.randomUUID();
		OwnerReplyRequestDto requestDto = new OwnerReplyRequestDto("사장님 답글입니다.");

		UserEntity nonOwner = UserEntity.builder()
			.userId(UUID.randomUUID())
			.name("고객")
			.role(UserEntity.UserRole.CUSTOMER)
			.build();

		TestingAuthenticationToken authentication = new TestingAuthenticationToken(nonOwner, null);
		authentication.setAuthenticated(true);

		mockMvc.perform(patch("/api/reviews/{reviewId}/owner-reply", reviewId)
				.principal(authentication)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isForbidden())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value("REVIEW_OWNER_UNAUTHORIZED"))
			.andExpect(jsonPath("$.message").value("사장님 본인만 해당 리뷰에 답글을 작성/수정/삭제할 수 있습니다."));
	}
}
