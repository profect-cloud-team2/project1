package com.example.demo.admin;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.entity.AdminReport;
import com.example.demo.admin.entity.ReportStatus;
import com.example.demo.admin.exception.UnauthorizedReportAccessException;
import com.example.demo.admin.repository.AdminReportRepository;
import com.example.demo.admin.service.AdminReportService;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminReportServiceTest {

	@Mock
	private AdminReportRepository adminReportRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AdminReportService adminReportService;

	@Test
	void createReport_정상생성() {
		// given
		UUID reporterId = UUID.randomUUID();
		UUID reportedId = UUID.randomUUID();

		AdminReportRequestDto dto = AdminReportRequestDto.builder()
			.reportedId(reportedId)
			.reportType("USER")
			.content("불법적인 활동")
			.build();

		when(adminReportRepository.save(any())).thenAnswer(invocation -> {
			AdminReport report = invocation.getArgument(0);
			report.setReportId(UUID.randomUUID());
			return report;
		});

		// when
		AdminReportResponseDto response = adminReportService.createReport(dto, reporterId);

		// then
		ArgumentCaptor<AdminReport> captor = ArgumentCaptor.forClass(AdminReport.class);
		verify(adminReportRepository).save(captor.capture());

		AdminReport saved = captor.getValue();
		assertThat(saved.getReporter().getUserId()).isEqualTo(reporterId);
		assertThat(saved.getReported().getUserId()).isEqualTo(reportedId);
		assertThat(saved.getReportType()).isEqualTo("USER");
		assertThat(saved.getContent()).isEqualTo("불법적인 활동");
		assertThat(saved.getStatus()).isEqualTo(ReportStatus.PENDING);

		System.out.println("🧪 신고 생성 테스트 결과\n신고자: " + response.getReporter().getUserId() +
			"\n피신고자: " + response.getReported().getUserId() +
			"\n유형: " + response.getReportType() +
			"\n내용: " + response.getContent());
	}

	@Test
	void deleteReport_관리자가_삭제요청하면_정상작동() {
		// given
		UUID reportId = UUID.randomUUID();
		UUID adminId = UUID.randomUUID();

		UserEntity admin = UserEntity.builder()
			.userId(adminId)
			.name("관리자")
			.birthdate(LocalDate.of(1990, 1, 1))
			.phone("01012345678")
			.email("admin@example.com")
			.loginId("admin")
			.password("pw")
			.nickname("관리자")
			.createdBy(adminId)
			.role(UserEntity.UserRole.ADMIN)
			.build();

		AdminReport report = AdminReport.builder()
			.reportId(reportId)
			.reporter(admin)
			.reported(admin)
			.reportType("USER")
			.content("스팸")
			.status(ReportStatus.PENDING)
			.createdBy(adminId)
			.createdAt(LocalDateTime.now())
			.build();

		when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
		when(adminReportRepository.findById(reportId)).thenReturn(Optional.of(report));

		// when
		adminReportService.deleteReport(reportId, adminId);

		// then
		assertThat(report.getDeletedAt()).isNotNull();
		assertThat(report.getDeletedBy()).isEqualTo(adminId);

		System.out.println("🧪 신고 삭제 테스트 결과\n삭제 시간: " + report.getDeletedAt() +
			"\n삭제자 ID: " + report.getDeletedBy());
	}

	@Test
	void deleteReport_관리자아닌경우_예외발생() {
		// given
		UUID reportId = UUID.randomUUID();
		UUID nonAdminId = UUID.randomUUID();

		UserEntity nonAdmin = UserEntity.builder()
			.userId(nonAdminId)
			.name("유저")
			.birthdate(LocalDate.of(1995, 5, 5))
			.phone("01011112222")
			.email("user@example.com")
			.loginId("user")
			.password("pw")
			.nickname("유저")
			.createdBy(nonAdminId)
			.role(UserEntity.UserRole.CUSTOMER)
			.build();

		when(userRepository.findById(nonAdminId)).thenReturn(Optional.of(nonAdmin));

		// when & then
		assertThrows(UnauthorizedReportAccessException.class, () -> {
			adminReportService.deleteReport(reportId, nonAdminId);
		});

		System.out.println("❌ 관리자 권한 없는 유저가 삭제 요청 시 예외 발생 확인 완료");
	}
}
