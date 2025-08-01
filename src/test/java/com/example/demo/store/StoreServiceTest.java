package com.example.demo.store;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StoreServiceTest {

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void 가게등록_및_수정테스트() {
		UserEntity testUser = UserEntity.builder()
			.name("테스트유저")
			.birthdate(LocalDate.of(1995, 1, 1))
			.phone("01012345678")
			.email("test@example.com")
			.loginId("testuser")
			.password("encodedPassword")
			.nickname("테스트닉")
			.createdBy(UUID.randomUUID())
			.role(UserEntity.UserRole.OWNER)
			.build();
		testUser = userRepository.save(testUser);

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

		StoreResponseDto saved = storeService.createStore(createDto, testUser.getUserId().toString());
		UUID storeId = saved.getStoreId();

		assertThat(saved.getName()).isEqualTo("테스트치킨");
		assertThat(saved.getAiDescription()).isNotBlank();

		System.out.println("🧪 가게 등록 테스트 결과");
		System.out.printf(" - 이름: %s%n", saved.getName());
		System.out.printf(" - AI 소개글: %s%n%n", saved.getAiDescription());

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

		storeService.updateStore(storeId, updateDto);

		Optional<StoreEntity> updatedOpt = storeRepository.findById(storeId);
		assertThat(updatedOpt).isPresent();
		StoreEntity updated = updatedOpt.get();

		assertThat(updated.getName()).isEqualTo("수정된치킨");
		assertThat(updated.getPhoneNum()).isEqualTo("01098765432");
		assertThat(updated.getIntroduction()).isEqualTo("수정된 가게 소개");
		assertThat(updated.getAiDescription()).isNotBlank();

		System.out.println("🧪 가게 수정 테스트 결과");
		System.out.printf(" - 수정 이름: %s%n", updated.getName());
		System.out.printf(" - 전화번호: %s%n", updated.getPhoneNum());
		System.out.printf(" - 소개글: %s%n", updated.getIntroduction());
		System.out.printf(" - AI 소개글: %s%n%n", updated.getAiDescription());
	}
}
