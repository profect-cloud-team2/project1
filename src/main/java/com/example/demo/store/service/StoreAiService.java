package com.example.demo.store.service;

import com.example.demo.store.dto.StoreAiResponseDto;
import com.example.demo.store.entity.Category;
import com.example.demo.store.service.ai.OpenAiClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class StoreAiService {
	private final OpenAiClient openAiClient;

	public StoreAiResponseDto generateAiDescription(String name, Category category) {
		String prompt = String.format(
			"가게 이름은 '%s'이고, 음식 카테고리는 '%s'입니다. 이 가게를 간단히 소개해 주세요. " +
				"답변을 최대한 간결하게 50자 이하로 해주세요. 답변은 큰따옴표 없이 문장으로 바로 시작해 주세요.",
			name, category.getDescription()
		);

		String rawResponse = openAiClient.getCompletion(prompt);
		String answerOnly = rawResponse.trim();

		return new StoreAiResponseDto(prompt, answerOnly);
	}
}
