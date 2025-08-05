package com.example.demo.review;

import com.example.demo.review.dto.OwnerReplyRequestDto;
import com.example.demo.user.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("사장이 아닌 유저가 답글 작성 시 403 예외 발생")
	void 답글_권한없음_예외() throws Exception {
		// given
		OwnerReplyRequestDto dto = new OwnerReplyRequestDto();
		ReflectionTestUtils.setField(dto, "replyContent", "사장님의 답글입니다.");

		String json = objectMapper.writeValueAsString(dto);

		UUID mockUserId = UUID.randomUUID();
		UserEntity mockUser = new UserEntity();
		ReflectionTestUtils.setField(mockUser, "userId", mockUserId);
		ReflectionTestUtils.setField(mockUser, "role", UserEntity.UserRole.CUSTOMER);

		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(mockUser, null, List.of());

		// when & then
		mockMvc.perform(patch("/api/reviews/{reviewId}/owner-reply", UUID.randomUUID())
				.with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isForbidden()); // 403 권한 없음
	}
}
