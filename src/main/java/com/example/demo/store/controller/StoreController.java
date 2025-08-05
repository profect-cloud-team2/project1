package com.example.demo.store.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.store.dto.*;
import com.example.demo.store.service.StoreService;
import com.example.demo.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@Operation(summary = "가게 등록", description = "점주가 가게 정보를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "등록 성공"),
			@ApiResponse(responseCode = "409", description = "중복된 가게")
		}
	)
	@PostMapping
	public ResponseEntity<?> registerStore(
		@AuthenticationPrincipal UserEntity user,
		@Valid @RequestBody StoreCreateRequestDto dto
	) {
		StoreResponseDto response = storeService.createStore(dto, user);
		return ResponseEntity.status(201).body(response);
	}

	@Operation(summary = "가게 수정", description = "점주가 기존 가게 정보를 수정합니다. 상태는 OPEN 또는 PREPARE로만 변경 가능합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 가게"),
			@ApiResponse(responseCode = "400", description = "잘못된 상태 값")
		}
	)
	@PatchMapping("/{storeId}")
	public ResponseEntity<?> updateStore(
		@PathVariable UUID storeId,
		@Valid @RequestBody StoreUpdateRequestDto dto,
		@AuthenticationPrincipal UserEntity user
	) {
		StoreResponseDto updated = storeService.updateStore(storeId, dto, user);
		return ResponseEntity.ok(Map.of(
			"message", "가게 정보가 성공적으로 수정되었습니다.",
			"store", updated
		));
	}

	@Operation(summary = "가게 폐업 신청", description = "점주가 가게 폐업을 신청합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "폐업 요청 성공"),
			@ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음")
		}
	)
	@DeleteMapping("/{storeId}")
	public ResponseEntity<?> requestStoreClosure(
		@PathVariable UUID storeId,
		@RequestBody StoreDeleteRequestDto dto,
		@AuthenticationPrincipal UserEntity user
	) {
		storeService.requestStoreClosure(storeId, user, dto.getReason());
		return ResponseEntity.ok("폐업 요청이 접수되었습니다.");
	}
}
