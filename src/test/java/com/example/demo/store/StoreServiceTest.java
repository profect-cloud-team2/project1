package com.example.demo.store;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.store.service.StoreService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoreServiceTest {

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreRepository storeRepository;

	@Test
	void 가게등록_및_수정테스트() {
		// 1. 가게 등록용 DTO 생성
		StoreCreateRequestDto createDto = new StoreCreateRequestDto();
		createDto.setName("테스트치킨");
		createDto.setCategory("치킨");
		createDto.setAddress1("서울 강남구");
		createDto.setAddress2("101호");
		createDto.setPhoneNum("01012345678");
		createDto.setImgURL("https://example.com/image.jpg");
		createDto.setOpenTime("11:00");
		createDto.setClosedTime("23:00");

		// 2. 가게 등록
		StoreResponseDto saved = storeService.createStore(createDto);
		UUID storeId = saved.getStoreId();

		assertThat(saved.getName()).isEqualTo("테스트치킨");
		assertThat(saved.getAiDescription()).isNotBlank(); // GPT 설명 생성 확인

		// 3. 수정용 DTO 생성
		StoreUpdateRequestDto updateDto = new StoreUpdateRequestDto();
		updateDto.setName("수정된치킨");
		updateDto.setCategory("한식");
		updateDto.setAddress1("서울 강남구");
		updateDto.setAddress2("202호");
		updateDto.setPhoneNum("01098765432");
		updateDto.setImgURL("https://example.com/newimage.jpg");
		updateDto.setOpenTime("10:00");
		updateDto.setClosedTime("22:00");

		// 4. 수정 실행
		storeService.updateStore(storeId, updateDto);

		// 5. 다시 조회 후 검증
		Optional<StoreEntity> updatedOpt = storeRepository.findById(storeId);
		assertThat(updatedOpt).isPresent();
		StoreEntity updated = updatedOpt.get();

		assertThat(updated.getName()).isEqualTo("수정된치킨");
		assertThat(updated.getPhoneNum()).isEqualTo("01098765432");
		assertThat(updated.getAiDescription()).isNotBlank();
	}
}
