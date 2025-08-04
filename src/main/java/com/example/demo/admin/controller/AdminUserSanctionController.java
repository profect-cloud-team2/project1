package com.example.demo.admin.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.admin.dto.AdminUserSanctionRequestDto;
import com.example.demo.admin.dto.AdminUserSanctionResponseDto;
import com.example.demo.admin.service.AdminUserSanctionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/sanctions")
public class AdminUserSanctionController {

	private final AdminUserSanctionService sanctionService;
	@Operation(summary = "유저 제재 생성", description = "관리자가 유저에 대해 제재를 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "제재 생성 성공"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 입력"),
		@ApiResponse(responseCode = "403", description = "관리자 권한 없음")
	})
	@PostMapping
	public ResponseEntity<AdminUserSanctionResponseDto> createSanction(
		@RequestBody AdminUserSanctionRequestDto dto,
		@AuthenticationPrincipal String adminId
	) {
		return ResponseEntity.ok(
			sanctionService.createSanction(dto, UUID.fromString(adminId))
		);
	}
	@Operation(summary = "모든 제재 목록 조회", description = "관리자가 전체 유저 제재 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "제재 목록 조회 성공"),
		@ApiResponse(responseCode = "403", description = "관리자 권한 없음")
	})
	@GetMapping
	public ResponseEntity<List<AdminUserSanctionResponseDto>> getAllSanctions() {
		return ResponseEntity.ok(sanctionService.getAllSanctions());
	}
	@Operation(summary = "제재 삭제 처리", description = "관리자가 유저에 대한 제재를 soft delete 처리합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "제재 삭제 처리 성공"),
		@ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
		@ApiResponse(responseCode = "404", description = "해당 제재 내역 없음")
	})
	@PatchMapping("/{id}/delete")
	public ResponseEntity<?> deleteSanction(@PathVariable UUID id, @AuthenticationPrincipal String adminId) {
		sanctionService.deleteSanction(id, UUID.fromString(adminId));
		return ResponseEntity.ok("제재 삭제 완료");
	}
}
