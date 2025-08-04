package com.example.demo.admin.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.store.dto.StoreDeleteRequestDto;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
public class AdminStoreController {

	private final StoreService storeService;

	// ✅ 폐업 승인
	@PatchMapping("/{storeId}/approve-close")
	public ResponseEntity<?> approveClose(
		@PathVariable UUID storeId,
		@AuthenticationPrincipal UserEntity admin
	) {
		try {
			storeService.approveStoreClosure(storeId, admin);
			return ResponseEntity.ok("폐업이 승인되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// ✅ 폐업 거절
	@PatchMapping("/{storeId}/reject-closure")
	public ResponseEntity<?> rejectClosure(
		@PathVariable UUID storeId,
		@RequestBody StoreDeleteRequestDto dto,
		@AuthenticationPrincipal UserEntity admin
	) {
		try {
			storeService.rejectStoreClosure(storeId, admin, dto.getReason());
			return ResponseEntity.ok("폐업 신청이 거절되었습니다.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// ✅ 강제 삭제
	@PatchMapping("/{storeId}/delete")
	public ResponseEntity<?> forceDelete(
		@PathVariable UUID storeId,
		@RequestBody StoreDeleteRequestDto dto,
		@AuthenticationPrincipal UserEntity admin
	) {
		try {
			storeService.forceDeleteStore(storeId, admin, dto.getReason());
			return ResponseEntity.ok("가게가 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
