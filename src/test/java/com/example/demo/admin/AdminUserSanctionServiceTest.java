package com.example.demo.admin;

import com.example.demo.admin.dto.AdminUserSanctionRequestDto;
import com.example.demo.admin.dto.AdminUserSanctionResponseDto;
import com.example.demo.admin.entity.AdminUserSanction;
import com.example.demo.admin.entity.SanctionStatus;
import com.example.demo.admin.repository.AdminUserSanctionRepository;
import com.example.demo.admin.service.AdminUserSanctionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserSanctionServiceTest {

	@Mock
	private AdminUserSanctionRepository repository;

	@InjectMocks
	private AdminUserSanctionService service;

	@Test
	void createSanction_정상생성() {
		// given
		UUID userId = UUID.randomUUID();
		UUID adminId = UUID.randomUUID();
		UUID sanctionId = UUID.randomUUID();

		AdminUserSanctionRequestDto requestDto = AdminUserSanctionRequestDto.builder()
			.userId(userId)
			.reason("테스트 사유")
			.sanctionStatus(SanctionStatus.SUSPEND)
			.note("테스트 비고")
			.startDate(LocalDateTime.of(2025, 8, 1, 0, 0))
			.endDate(LocalDateTime.of(2025, 8, 7, 0, 0))
			.build();

		when(repository.save(any())).thenAnswer(invocation -> {
			AdminUserSanction sanction = invocation.getArgument(0);
			sanction.setSanctionId(sanctionId);
			return sanction;
		});

		// when
		AdminUserSanctionResponseDto response = service.createSanction(requestDto, adminId);

		// then
		ArgumentCaptor<AdminUserSanction> captor = ArgumentCaptor.forClass(AdminUserSanction.class);
		verify(repository).save(captor.capture());
		AdminUserSanction saved = captor.getValue();

		// assertions
		assertThat(saved.getUserId()).isEqualTo(userId);
		assertThat(saved.getCreatedBy()).isEqualTo(adminId);
		assertThat(saved.getSanctionStatus()).isEqualTo(SanctionStatus.SUSPEND);
		assertThat(saved.getStartDate()).isEqualTo(requestDto.getStartDate());
		assertThat(saved.getEndDate()).isEqualTo(requestDto.getEndDate());

		assertThat(response.getUserId()).isEqualTo(userId);
		assertThat(response.getSanctionId()).isEqualTo(sanctionId);

		// 🧪 로그 출력
		System.out.println("🧪 제재 생성 테스트 결과");
		System.out.println("유저 ID: " + response.getUserId());
		System.out.println("제재 ID: " + response.getSanctionId());
		System.out.println("제재 상태: " + response.getSanctionStatus());
		System.out.println("시작일: " + response.getStartDate());
		System.out.println("종료일: " + response.getEndDate());
		System.out.println("비고: " + response.getNote());
	}
}
