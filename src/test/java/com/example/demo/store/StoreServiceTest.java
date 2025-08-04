package com.example.demo.store;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.store.service.StoreAiService;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

public class StoreServiceTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private StoreAiService storeAiService;

	@InjectMocks
	private StoreService storeService;

	private UserEntity testUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		testUser = UserEntity.builder()
			.userId(UUID.randomUUID())
			.name("테스트유저")
			.birthdate(LocalDate.of(1995, 1, 1))
			.phone("01012345678")
			.email("test@example.com")
			.loginId("testuser")
			.password("encodedPassword")
			.nickname("테스트닉")
			.role(UserEntity.UserRole.OWNER)
			.build();

		// ✅ 핵심: createStore에서 사용하는 메서드 정확히 mock 처리
		when(userRepository.findByUserIdAndDeletedAtIsNull(any(UUID.class)))
			.thenReturn(Optional.of(testUser));

		when(storeAiService.generateAiDescription(any(), any()))
			.thenReturn("AI 설명 예시");

		when(storeRepository.save(any(StoreEntity.class)))
			.thenAnswer(invocation -> {
				StoreEntity entity = invocation.getArgument(0);
				if (entity.getStoreId() == null) {
					entity.setStoreId(UUID.randomUUID());
				}
				return entity;
			});
	}

	@Test
	void 가게등록_및_수정테스트() {
		// 등록 요청 DTO
		StoreCreateRequestDto createDto = new StoreCreateRequestDto();
		createDto.setName("테스트치킨");
		createDto.setBusinessNum("1234567890");
		createDto.setCategory(Category.KOREAN);
		createDto.setAddress1("서울 강남구");
		createDto.setAddress2("101호");
		createDto.setPhoneNum("01012345678");
		createDto.setImgURL("https://example.com/image.jpg");
		createDto.setOpenTime("11:00");
		createDto.setClosedTime("23:00");
		createDto.setIntroduction("테스트 가게 소개");
		createDto.setIsAvailable(StoreStatus.OPEN);

		// 등록 실행
		var saved = storeService.createStore(createDto, testUser);

		assertThat(saved.getName()).isEqualTo("테스트치킨");
		assertThat(saved.getAiDescription()).isEqualTo("AI 설명 예시");

		// 수정 요청 DTO
		StoreUpdateRequestDto updateDto = new StoreUpdateRequestDto();
		updateDto.setName("수정된치킨");
		updateDto.setCategory(Category.KOREAN);
		updateDto.setAddress1("서울 강남구");
		updateDto.setAddress2("202호");
		updateDto.setPhoneNum("01098765432");
		updateDto.setImgURL("https://example.com/newimage.jpg");
		updateDto.setOpenTime("10:00");
		updateDto.setClosedTime("22:00");
		updateDto.setIntroduction("수정된 가게 소개");
		updateDto.setIsAvailable(StoreStatus.CLOSED);

		// 수정용 가짜 가게 엔티티
		StoreEntity existing = StoreEntity.builder()
			.storeId(saved.getStoreId())
			.user(testUser)
			.name(saved.getName())
			.phoneNum(saved.getPhoneNum())
			.category(Category.KOREAN)
			.build();

		when(storeRepository.findByStoreIdAndDeletedAtIsNull(saved.getStoreId()))
			.thenReturn(Optional.of(existing));

		// 수정 실행
		storeService.updateStore(saved.getStoreId(), updateDto, testUser);

		// 검증
		ArgumentCaptor<StoreEntity> captor = ArgumentCaptor.forClass(StoreEntity.class);
		verify(storeRepository, atLeast(2)).save(captor.capture());

		StoreEntity updated = captor.getValue();

		assertThat(updated.getName()).isEqualTo("수정된치킨");
		assertThat(updated.getPhoneNum()).isEqualTo("01098765432");
		assertThat(updated.getIntroduction()).isEqualTo("수정된 가게 소개");
		assertThat(updated.getAiDescription()).isEqualTo("AI 설명 예시");
		System.out.println("\n🧪 가게 등록 및 수정 테스트 결과:");
		System.out.println(" - 등록 이름: " + saved.getName());
		System.out.println(" - 수정 이름: " + updated.getName());
		System.out.println(" - 전화번호: " + updated.getPhoneNum());
		System.out.println(" - 소개글: " + updated.getIntroduction());
		System.out.println(" - AI 설명: " + updated.getAiDescription() + "\n");
	}
}
