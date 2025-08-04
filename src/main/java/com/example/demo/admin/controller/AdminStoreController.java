package com.example.demo.admin.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.store.dto.StoreDeleteRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
public class AdminStoreController {

	private final StoreService storeService;

	@Operation(summary = "심사 대기 중인 가게 목록 조회", description = "등록 심사를 기다리는 가게 목록을 페이지 단위로 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "심사 대기 가게 목록 조회 성공")
	})
	@GetMapping("/pending")
	public ResponseEntity<Page<StoreResponseDto>> getPendingStores(
		@Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(storeService.getStoresByStatusUnderReview(page, size));
	}

	@Operation(summary = "관리자 - 심사중 가게 목록 조회", description = "현재 심사 상태가 'UNDER_REVIEW'인 가게 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "심사 중 가게 목록 조회 성공")
	})
	@GetMapping("/admin/reviewing")
	public ResponseEntity<Page<StoreResponseDto>> getUnderReviewStores(
		@Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
	) {
		Page<StoreResponseDto> stores = storeService.getStoresByStatusUnderReview(page, size);
		return ResponseEntity.ok(stores);
	}

	@Operation(summary = "가게 등록 승인", description = "관리자가 가게 등록 요청을 승인합니다.")
	@PatchMapping("/{storeId}/approve")
	public ResponseEntity<?> approveStore(
		@Parameter(description = "승인할 가게 ID") @PathVariable UUID storeId,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity admin
	) {
		storeService.approveStore(storeId, admin.getUserId());
		return ResponseEntity.ok("가게 등록이 승인되었습니다.");
	}

	@Operation(summary = "가게 등록 거절", description = "관리자가 가게 등록 요청을 거절합니다.")
	@PatchMapping("/{storeId}/reject")
	public ResponseEntity<?> rejectStore(
		@Parameter(description = "거절할 가게 ID") @PathVariable UUID storeId,
		@Parameter(description = "거절 사유") @RequestParam String reason,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity admin
	) {
		storeService.rejectStore(storeId, reason, admin.getUserId());
		return ResponseEntity.ok("가게 등록이 거절되었습니다.");
	}

	@Operation(summary = "가게 폐업 승인", description = "관리자가 점주의 폐업 요청을 승인합니다.")
	@PatchMapping("/{storeId}/approve-close")
	public ResponseEntity<?> approveClose(
		@Parameter(description = "폐업 승인할 가게 ID") @PathVariable UUID storeId,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity admin
	) {
		try {
			storeService.approveStoreClosure(storeId, admin);
			return ResponseEntity.ok("폐업이 승인되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "가게 폐업 거절", description = "관리자가 점주의 폐업 요청을 거절합니다.")
	@PatchMapping("/{storeId}/reject-closure")
	public ResponseEntity<?> rejectClosure(
		@Parameter(description = "폐업 거절할 가게 ID") @PathVariable UUID storeId,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "폐업 거절 사유 DTO", required = true)
		@RequestBody StoreDeleteRequestDto dto,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity admin
	) {
		try {
			storeService.rejectStoreClosure(storeId, admin, dto.getReason());
			return ResponseEntity.ok("폐업 신청이 거절되었습니다.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "가게 강제 삭제", description = "관리자가 가게를 강제로 삭제합니다.")
	@PatchMapping("/{storeId}/delete")
	public ResponseEntity<?> forceDelete(
		@Parameter(description = "삭제할 가게 ID") @PathVariable UUID storeId,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "삭제 사유 DTO", required = true)
		@RequestBody StoreDeleteRequestDto dto,
		@Parameter(hidden = true) @AuthenticationPrincipal UserEntity admin
	) {
		try {
			storeService.forceDeleteStore(storeId, admin, dto.getReason());
			return ResponseEntity.ok("가게가 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
