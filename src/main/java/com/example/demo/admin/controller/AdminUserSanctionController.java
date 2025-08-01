package com.example.demo.admin.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.admin.dto.AdminUserSanctionRequestDto;
import com.example.demo.admin.dto.AdminUserSanctionResponseDto;
import com.example.demo.admin.service.AdminUserSanctionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/sanctions")
public class AdminUserSanctionController {

	private final AdminUserSanctionService sanctionService;

	@PostMapping
	public ResponseEntity<AdminUserSanctionResponseDto> createSanction(
		@RequestBody AdminUserSanctionRequestDto dto,
		@AuthenticationPrincipal String adminId
	) {
		return ResponseEntity.ok(
			sanctionService.createSanction(dto, UUID.fromString(adminId))
		);
	}

	@GetMapping
	public ResponseEntity<List<AdminUserSanctionResponseDto>> getAllSanctions() {
		return ResponseEntity.ok(sanctionService.getAllSanctions());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSanction(@PathVariable UUID id, @AuthenticationPrincipal String adminId) {
		sanctionService.deleteSanction(id, UUID.fromString(adminId));
		return ResponseEntity.ok("제재 삭제 완료");
	}
}
