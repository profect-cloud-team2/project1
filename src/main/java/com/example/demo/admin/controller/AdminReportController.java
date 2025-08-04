package com.example.demo.admin.controller;

import java.util.Map;
import java.util.UUID;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.dto.AdminReportUpdateRequestDto;
import com.example.demo.admin.service.AdminReportService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
public class AdminReportController {

	private final AdminReportService adminReportService;

	@PostMapping
	public ResponseEntity<AdminReportResponseDto> createReport(
		@RequestBody AdminReportRequestDto dto,
		@AuthenticationPrincipal UserEntity user
	) {
		AdminReportResponseDto response = adminReportService.createReport(dto, user.getUserId());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{reportId}")
	public ResponseEntity<AdminReportResponseDto> getReportById(@PathVariable UUID reportId) {
		AdminReportResponseDto response = adminReportService.getReportById(reportId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{reportId}/status")
	public ResponseEntity<AdminReportResponseDto> updateReportStatus(
		@PathVariable UUID reportId,
		@RequestBody AdminReportUpdateRequestDto dto,
		@AuthenticationPrincipal UserEntity user
	) {
		AdminReportResponseDto response = adminReportService.updateReportStatus(reportId, dto.getStatus(), user.getUserId());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{reportId}")
	public ResponseEntity<Map<String, String>> deleteReport(
		@PathVariable UUID reportId,
		@AuthenticationPrincipal UserEntity user
	) {
		adminReportService.deleteReport(reportId, user.getUserId());
		return ResponseEntity.ok().body(Map.of("message", "신고 내역이 삭제되었습니다."));
	}
}
