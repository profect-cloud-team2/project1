package com.example.demo.store;

import com.example.demo.global.exception.GlobalExceptionHandler;
import com.example.demo.store.controller.StoreController;
import com.example.demo.store.dto.*;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.exception.UnauthorizedStoreAccessException;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // ✅ 추가

public class StoreControllerTest {

	@InjectMocks
	private StoreController storeController;

	@Mock
	private StoreService storeService;

	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private UserEntity testUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(storeController)
			.setControllerAdvice(new GlobalExceptionHandler()) // ✅ 예외 핸들러 등록
			.alwaysDo(print())
			.build();


		testUser = UserEntity.builder()
			.userId(UUID.randomUUID())
			.nickname("테스트유저")
			.role(UserEntity.UserRole.OWNER)
			.build();
	}

	@Test
	void 가게등록_성공() throws Exception {
		StoreCreateRequestDto dto = new StoreCreateRequestDto();
		dto.setName("테스트가게");
		dto.setBusinessNum("1234567890");
		dto.setCategory(Category.KOREAN);
		dto.setAddress1("서울");
		dto.setAddress2("101호");
		dto.setPhoneNum("01012345678");
		dto.setOpenTime("10:00");
		dto.setClosedTime("22:00");
		dto.setIntroduction("테스트용");
		dto.setImgURL("http://image.com");
		dto.setIsAvailable(StoreStatus.OPEN);

		StoreResponseDto responseDto = new StoreResponseDto(
			UUID.randomUUID(),
			testUser.getUserId(),
			dto.getName(),
			dto.getBusinessNum(),
			dto.getCategory(),
			dto.getAddress1(),
			dto.getAddress2(),
			dto.getPhoneNum(),
			dto.getImgURL(),
			LocalTime.parse(dto.getOpenTime()),
			LocalTime.parse(dto.getClosedTime()),
			"AI 설명",
			dto.getIntroduction(),
			null,
			null,
			StoreStatus.OPEN
		);

		when(storeService.createStore(any(), any())).thenReturn(responseDto);

		mockMvc.perform(post("/api/store")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
				.requestAttr("user", testUser)) // AuthenticationPrincipal 대체
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value("테스트가게"));
	}

	@Test
	void 가게수정_권한없음_예외() throws Exception {
		UUID storeId = UUID.randomUUID();
		StoreUpdateRequestDto updateDto = new StoreUpdateRequestDto();
		updateDto.setName("수정된이름");
		updateDto.setCategory(Category.KOREAN);
		updateDto.setAddress1("서울");
		updateDto.setAddress2("202호");
		updateDto.setPhoneNum("01099998888");
		updateDto.setImgURL("http://image2.com");
		updateDto.setOpenTime("09:00");
		updateDto.setClosedTime("21:00");
		updateDto.setIntroduction("수정 소개");
		updateDto.setIsAvailable(StoreStatus.OPEN);

		when(storeService.updateStore(eq(storeId), any(StoreUpdateRequestDto.class), any()))
			.thenThrow(new UnauthorizedStoreAccessException());

		mockMvc.perform(patch("/api/store/" + storeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto))
				.requestAttr("user", testUser))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.message").value("권한이 없습니다."));
	}
}
