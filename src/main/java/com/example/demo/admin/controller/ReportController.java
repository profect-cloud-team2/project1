package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.service.AdminReportService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

	private final AdminReportService adminReportService;

	@PostMapping
	public ResponseEntity<AdminReportResponseDto> createReport(
		@RequestBody AdminReportRequestDto dto,
		@AuthenticationPrincipal UserEntity user
	) {
		if (user == null || user.getUserId() == null) {
			return ResponseEntity.status(401).build();
		}

		AdminReportResponseDto response = adminReportService.createReport(dto, user.getUserId());
		return ResponseEntity.ok(response);
	}
}
