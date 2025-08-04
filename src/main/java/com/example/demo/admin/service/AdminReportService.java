package com.example.demo.admin.service;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.entity.AdminReport;
import com.example.demo.admin.entity.ReportStatus;
import com.example.demo.admin.exception.ReportAlreadyDeletedException;
import com.example.demo.admin.exception.ReportNotFoundException;
import com.example.demo.admin.exception.UnauthorizedReportAccessException;
import com.example.demo.admin.repository.AdminReportRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminReportService {

	private final AdminReportRepository adminReportRepository;
	private final UserRepository userRepository;

	public AdminReportResponseDto createReport(AdminReportRequestDto dto, UUID reporterId) {
		AdminReport report = AdminReport.builder()
			.reporter(UserEntity.builder().userId(reporterId).build())
			.reported(UserEntity.builder().userId(dto.getReportedId()).build())
			.reportType(dto.getReportType())
			.content(dto.getContent())
			.status(ReportStatus.PENDING)
			.createdAt(LocalDateTime.now())
			.createdBy(reporterId)
			.build();

		AdminReport saved = adminReportRepository.save(report);
		return AdminReportResponseDto.fromEntity(saved);
	}

	public AdminReportResponseDto updateReportStatus(UUID reportId, ReportStatus newStatus, UUID adminId) {
		AdminReport report = adminReportRepository.findById(reportId)
			.orElseThrow(ReportNotFoundException::new);

		report.setStatus(newStatus);
		report.setUpdatedBy(adminId);
		report.setUpdatedAt(LocalDateTime.now());

		AdminReport updated = adminReportRepository.save(report);
		return AdminReportResponseDto.fromEntity(updated);
	}

	public AdminReportResponseDto getReportById(UUID reportId) {
		AdminReport report = adminReportRepository.findById(reportId)
			.orElseThrow(ReportNotFoundException::new);
		return AdminReportResponseDto.fromEntity(report);
	}

	public void deleteReport(UUID reportId, UUID userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다.")); // 이건 사용자 공통 예외로 남겨도 괜찮습니다

		if (user.getRole() != UserEntity.UserRole.ADMIN) {
			throw new UnauthorizedReportAccessException();
		}

		AdminReport report = adminReportRepository.findById(reportId)
			.orElseThrow(ReportNotFoundException::new);

		if (report.getDeletedAt() != null) {
			throw new ReportAlreadyDeletedException();
		}

		report.setDeletedAt(LocalDateTime.now());
		report.setDeletedBy(userId);

		adminReportRepository.save(report);
	}
}
