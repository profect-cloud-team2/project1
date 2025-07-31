package com.example.demo.store;

import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreEntityTest {

	@Test
	void softDelete_삭제_정상작동() {
		// given
		UUID userId = UUID.randomUUID();
		StoreEntity store = StoreEntity.builder()
			.name("삭제테스트")
			.isAvailable(StoreStatus.OPEN)
			.introduction("기존 소개글")
			.build();

		// when
		String reason = "관리자 권한 삭제";
		store.softDelete(StoreStatus.DELETED, reason, userId);

		// then
		assertThat(store.getDeletedAt()).isNotNull();
		assertThat(store.getDeletedBy()).isEqualTo(userId);
		assertThat(store.getIsAvailable()).isEqualTo(StoreStatus.DELETED);
		assertThat(store.getIntroduction()).contains(reason);

		// 출력
		System.out.println("🧪 삭제 테스트 결과");
		System.out.println("삭제 상태: " + store.getIsAvailable());
		System.out.println("소개글: " + store.getIntroduction());
	}

	@Test
	void softDelete_폐업신청_정상작동() {
		// given
		UUID userId = UUID.randomUUID();
		StoreEntity store = StoreEntity.builder()
			.name("폐업신청테스트")
			.isAvailable(StoreStatus.OPEN)
			.introduction("기존 소개글")
			.build();

		// when
		String reason = "운영 중단 예정";
		store.softDelete(StoreStatus.CLOSED_REQUESTED, reason, userId);

		// then
		assertThat(store.getDeletedAt()).isNotNull();
		assertThat(store.getDeletedBy()).isEqualTo(userId);
		assertThat(store.getIsAvailable()).isEqualTo(StoreStatus.CLOSED_REQUESTED);
		assertThat(store.getIntroduction()).contains(reason);

		// 출력
		System.out.println("🧪 폐업 신청 테스트 결과");
		System.out.println("삭제 상태: " + store.getIsAvailable());
		System.out.println("소개글: " + store.getIntroduction());
	}

	@Test
	void softDelete_폐업승인_정상작동() {
		// given
		UUID adminId = UUID.randomUUID();
		StoreEntity store = StoreEntity.builder()
			.name("폐업승인테스트")
			.isAvailable(StoreStatus.CLOSED_REQUESTED)
			.introduction("[삭제됨] 운영 중단 예정")
			.build();

		// when
		String reason = "승인 처리됨";
		store.softDelete(StoreStatus.CLOSED, reason, adminId);

		// then
		assertThat(store.getDeletedAt()).isNotNull();
		assertThat(store.getDeletedBy()).isEqualTo(adminId);
		assertThat(store.getIsAvailable()).isEqualTo(StoreStatus.CLOSED);
		assertThat(store.getIntroduction()).contains(reason);

		// 출력
		System.out.println("🧪 폐업 승인 테스트 결과");
		System.out.println("삭제 상태: " + store.getIsAvailable());
		System.out.println("소개글: " + store.getIntroduction());
	}
	@Test
	void rejectClosure_폐업거절_정상작동() {
		// given
		UUID adminId = UUID.randomUUID();
		StoreEntity store = StoreEntity.builder()
			.name("거절대상치킨")
			.isAvailable(StoreStatus.CLOSED_REQUESTED)
			.introduction("[삭제됨] 사장님 요청으로 폐업 신청됨")
			.build();

		// when
		String reason = "신청 사유가 부적절함";
		store.rejectClosure(reason, adminId);

		// then
		assertThat(store.getIsAvailable()).isEqualTo(StoreStatus.OPEN);
		assertThat(store.getIntroduction()).startsWith("[폐업 거절]");
		assertThat(store.getIntroduction()).contains(reason);

		// 출력 확인용
		System.out.println("❌ 폐업 거절 테스트 결과");
		System.out.println("현재 상태: " + store.getIsAvailable());
		System.out.println("소개글: " + store.getIntroduction());
	}

}
