package com.example.demo.ai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ai.entity.AiEntity;
import com.example.demo.ai.service.AiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai-log")
@RequiredArgsConstructor
public class AiController {
	private final AiService aiService;

	/**
	 * 전체 로그 조회
	 * GET /api/ai-log
	 */
	@GetMapping
	public ResponseEntity<List<AiEntity>> getAll() {
		List<AiEntity> logs = aiService.getAllLogs();
		return ResponseEntity.ok(logs);
	}

	/**
	 * apiType 으로 필터링
	 * GET /api/ai-log?apiType=STORE_DESC
	 */
	@GetMapping(params = "apiType")
	public ResponseEntity<List<AiEntity>> getByType(@RequestParam String apiType) {
		List<AiEntity> logs = aiService.getLogsByApiType(apiType);
		return ResponseEntity.ok(logs);
	}
}
