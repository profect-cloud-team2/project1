package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

	private final AdminReportService adminReportService;

	@PostMapping
	public ResponseEntity<AdminReportResponseDto> createReport(
		@RequestBody AdminReportRequestDto dto,
		@AuthenticationPrincipal String userId // 신고자
	) {
		UUID reporterId = UUID.fromString(userId);
		AdminReportResponseDto response = adminReportService.createReport(dto, reporterId);
		return ResponseEntity.ok(response);
	}
}
