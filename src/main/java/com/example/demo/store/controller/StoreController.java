package com.example.demo.store.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreDeleteRequestDto;
import com.example.demo.store.dto.StoreResponseDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.store.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
	public ResponseEntity<?> registerStore(@AuthenticationPrincipal String userId,
		@Valid @RequestBody StoreCreateRequestDto dto) {
		try {
			StoreResponseDto response = storeService.createStore(dto, userId);
			return ResponseEntity.status(201).body(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}
	}

	@Operation(summary = "가게 수정", description = "점주가 기존 가게 정보를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 가게")
		}
	)
	@PatchMapping("/{storeId}")
	public ResponseEntity<?> updateStore(
		@PathVariable UUID storeId,
		@Valid @RequestBody StoreUpdateRequestDto dto) {
		try {
			StoreResponseDto updated = storeService.updateStore(storeId, dto);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "가게 정보가 성공적으로 수정되었습니다.");
			response.put("store", updated);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
		}
	}
	@DeleteMapping("/{storeId}")
	public ResponseEntity<?> requestStoreClosure(
		@PathVariable UUID storeId,
		@RequestBody StoreDeleteRequestDto dto,
		@AuthenticationPrincipal String userId
	) {
		try {
			storeService.requestStoreClosure(storeId, userId, dto.getReason());
			return ResponseEntity.ok("폐업 요청이 접수되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(403).body(e.getMessage());
		}
	}
}
