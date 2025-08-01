package com.example.demo.admin.service;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.entity.AdminReport;
import com.example.demo.admin.entity.ReportStatus;
import com.example.demo.admin.repository.AdminReportRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
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
			.reporter(UserEntity.builder().userId(reporterId).build()) // Lazy 참조용 껍데기
			.reported(UserEntity.builder().userId(dto.getReportedId()).build()) // 이 부분도 실제 엔티티로 참조
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
			.orElseThrow(() -> new IllegalArgumentException("해당 신고 내역이 없습니다."));

		report.setStatus(newStatus);
		report.setUpdatedBy(adminId);
		report.setUpdatedAt(LocalDateTime.now());

		AdminReport updated = adminReportRepository.save(report);
		return AdminReportResponseDto.fromEntity(updated);
	}

	public AdminReportResponseDto getReportById(UUID reportId) {
		AdminReport report = adminReportRepository.findById(reportId)
			.orElseThrow(() -> new IllegalArgumentException("신고 내역이 존재하지 않습니다."));
		return AdminReportResponseDto.fromEntity(report);
	}
	public void deleteReport(UUID reportId, UUID userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

		if (user.getRole() != UserEntity.UserRole.ADMIN) {
			throw new AccessDeniedException("관리자만 신고를 삭제할 수 있습니다.");
		}

		AdminReport report = adminReportRepository.findById(reportId)
			.orElseThrow(() -> new IllegalArgumentException("신고 내역이 존재하지 않습니다."));

		if (report.getDeletedAt() != null) {
			throw new IllegalStateException("이미 삭제된 신고입니다.");
		}

		report.setDeletedAt(LocalDateTime.now());
		report.setDeletedBy(userId);

		adminReportRepository.save(report);
	}
}
