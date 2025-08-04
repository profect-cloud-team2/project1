package com.example.demo.admin.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.store.dto.StoreDeleteRequestDto;
import com.example.demo.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
public class AdminStoreController {

	private final StoreService storeService;

	// 폐업 승인
	@PatchMapping("/{storeId}/approve-close")
	public ResponseEntity<?> approveClose(
		@PathVariable UUID storeId,
		@AuthenticationPrincipal String userId // 관리자 인증은 나중에
	) {
		try {
			storeService.approveStoreClosure(storeId, userId);
			return ResponseEntity.ok("폐업이 승인되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PatchMapping("/{storeId}/reject-closure")
	public ResponseEntity<?> rejectClosure(
		@PathVariable UUID storeId,
		@RequestBody StoreDeleteRequestDto dto,
		@AuthenticationPrincipal String adminId
	) {
		try {
			storeService.rejectStoreClosure(storeId, adminId, dto.getReason());
			return ResponseEntity.ok("폐업 신청이 거절되었습니다.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 강제 삭제
	@PatchMapping("/{storeId}/delete")
	public ResponseEntity<?> forceDelete(
		@PathVariable UUID storeId,
		@RequestBody StoreDeleteRequestDto dto,
		@AuthenticationPrincipal String userId // 관리자 인증은 나중에
	) {
		try {
			storeService.forceDeleteStore(storeId, userId, dto.getReason());
			return ResponseEntity.ok("가게가 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
