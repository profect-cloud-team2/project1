package com.example.demo.admin.controller;

import java.util.Map;
import java.util.UUID;

import com.example.demo.admin.dto.AdminReportRequestDto;
import com.example.demo.admin.dto.AdminReportResponseDto;
import com.example.demo.admin.dto.AdminReportUpdateRequestDto;
import com.example.demo.admin.service.AdminReportService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

	@Operation(summary = "신고 단건 조회", description = "신고 ID로 해당 신고 내역을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 조회 성공"),
		@ApiResponse(responseCode = "404", description = "신고가 존재하지 않음")
	})
	@GetMapping("/{reportId}")
	public ResponseEntity<AdminReportResponseDto> getReportById(
		@Parameter(description = "조회할 신고 ID") @PathVariable UUID reportId
	) {
		AdminReportResponseDto response = adminReportService.getReportById(reportId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "신고 상태 수정", description = "신고의 상태를 변경합니다. (e.g., 대기중 → 처리완료)")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 상태 수정 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "신고가 존재하지 않음")
	})
	@PatchMapping("/{reportId}/status")
	public ResponseEntity<AdminReportResponseDto> updateReportStatus(
		@Parameter(description = "상태를 변경할 신고 ID") @PathVariable UUID reportId,
		@RequestBody(description = "상태 변경 요청 DTO", required = true) AdminReportUpdateRequestDto dto,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity user
	) {
		AdminReportResponseDto response = adminReportService.updateReportStatus(reportId, dto.getStatus(), user.getUserId());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "신고 삭제", description = "신고 내역을 삭제합니다. 관리자만 삭제할 수 있습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "신고가 존재하지 않음")
	})
	@PatchMapping("/{reportId}/delete")
	public ResponseEntity<Map<String, String>> deleteReport(
		@Parameter(description = "삭제할 신고 ID") @PathVariable UUID reportId,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity user
	) {
		adminReportService.deleteReport(reportId, user.getUserId());
		return ResponseEntity.ok().body(Map.of("message", "신고 내역이 삭제되었습니다."));
	}
}
