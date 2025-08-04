package com.example.demo.ai.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.ai.entity.AiEntity;
import com.example.demo.ai.repository.AiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {
	private final AiRepository aiRepository;

	public List<AiEntity> getAllLogs() {
		return aiRepository.findAll();
	}

	public List<AiEntity> getLogsByApiType(String apiType) {
		return aiRepository.findByApiType(apiType);
	}
}
