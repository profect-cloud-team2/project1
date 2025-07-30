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

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoreServiceTest {

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserRepository userRepository;

	// @Test
	// void 가게등록_및_수정테스트() {
	// 	// 1. 테스트용 유저 생성 및 저장
	// 	UserEntity testUser = UserEntity.builder()
	// 		.name("테스트 유저")
	// 		.birthdate(LocalDate.of(1995, 1, 1))
	// 		.phone("01012345678")
	// 		.email("test@example.com")
	// 		.loginId("testuser")
	// 		.password("encodedPassword")
	// 		.nickname("테스트닉")
	// 		.createdBy(UUID.randomUUID())
	// 		.role(UserEntity.UserRole.OWNER)
	// 		.build();
	// 	testUser = userRepository.save(testUser);
	//
	//
	// 	// 2. 가게 등록 DTO 생성
	// 	StoreCreateRequestDto createDto = new StoreCreateRequestDto();
	// 	createDto.setUserId(testUser.getUserId());
	// 	createDto.setName("테스트치킨");
	// 	createDto.setBusinessNum("1234567890");
	// 	createDto.setCategory(Category.KOREAN);
	// 	createDto.setAddress1("서울 강남구");
	// 	createDto.setAddress2("101호");
	// 	createDto.setPhoneNum("01012345678");
	// 	createDto.setImgURL("https://example.com/image.jpg");
	// 	createDto.setOpenTime("11:00");
	// 	createDto.setClosedTime("23:00");
	//
	// 	// 3. 가게 등록 실행
	// 	StoreResponseDto saved = storeService.createStore(createDto, testUser.getUserId());
	// 	UUID storeId = saved.getStoreId();
	//
	// 	// 4. 등록 결과 검증
	// 	assertThat(saved.getName()).as("등록된 가게 이름이 일치해야 함").isEqualTo("테스트치킨");
	// 	assertThat(saved.getAiDescription()).as("AI 소개 문구가 생성되어야 함").isNotBlank();
	//
	// 	// 5. 가게 수정 DTO 생성
	// 	StoreUpdateRequestDto updateDto = new StoreUpdateRequestDto();
	// 	updateDto.setName("수정된치킨");
	// 	updateDto.setCategory(Category.KOREAN);
	// 	updateDto.setAddress1("서울 강남구");
	// 	updateDto.setAddress2("202호");
	// 	updateDto.setPhoneNum("01098765432");
	// 	updateDto.setImgURL("https://example.com/newimage.jpg");
	// 	updateDto.setOpenTime("10:00");
	// 	updateDto.setClosedTime("22:00");
	// 	updateDto.setIsAvailable(StoreStatus.CLOSED);
	//
	// 	// 6. 가게 수정 실행
	// 	storeService.updateStore(storeId, updateDto);
	//
	// 	// 7. DB에서 다시 조회하여 수정 결과 검증
	// 	Optional<StoreEntity> updatedOpt = storeRepository.findById(storeId);
	// 	assertThat(updatedOpt).isPresent();
	//
	// 	StoreEntity updated = updatedOpt.get();
	// 	assertThat(updated.getName()).as("수정된 이름 확인").isEqualTo("수정된치킨");
	// 	assertThat(updated.getPhoneNum()).as("수정된 전화번호 확인").isEqualTo("01098765432");
	// 	assertThat(updated.getAiDescription()).as("수정 후에도 AI 문구 존재").isNotBlank();
	// }
}
