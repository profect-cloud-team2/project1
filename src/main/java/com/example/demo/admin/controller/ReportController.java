package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.service.AdminReportService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

	@Operation(
		summary = "신고 생성",
		description = "유저가 다른 유저 또는 가게를 신고합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "신고 성공",
				content = @Content(schema = @Schema(implementation = AdminReportResponseDto.class))
			),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 유저"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
		}
	)
	@PostMapping
	public ResponseEntity<AdminReportResponseDto> createReport(
		@RequestBody(
			description = "신고 요청 DTO",
			required = true,
			content = @Content(schema = @Schema(implementation = AdminReportRequestDto.class))
		) AdminReportRequestDto dto,

		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity user
	) {
		if (user == null || user.getUserId() == null) {
			return ResponseEntity.status(401).build();
		}

		AdminReportResponseDto response = adminReportService.createReport(dto, user.getUserId());
		return ResponseEntity.ok(response);
	}
}
