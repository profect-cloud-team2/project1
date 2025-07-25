package com.example.demo.store.service;

import com.example.demo.store.service.ai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreAiService {

	private final OpenAiClient openAiClient;

	public String generateAiDescription(String storeName, String category) {
		String prompt = String.format(
			"가게 이름은 '%s'이고, 음식 카테고리는 '%s'입니다. 이 가게를 간단히 소개해 주세요. " +
				"답변을 최대한 간결하게 50자 이하로 해주세요.",
			storeName, category
		);

		// GPT 호출 대신 테스트용 응답 사용
		String response = openAiClient.getCompletion(prompt);
		System.out.println("📌 프롬프트: " + prompt);
		System.out.println("📌 GPT 응답 (Mock): " + response);

		return extractAnswerOnly(response);
	}

	private String extractAnswerOnly(String aiResponse) {
		return aiResponse.trim();
	}
}
