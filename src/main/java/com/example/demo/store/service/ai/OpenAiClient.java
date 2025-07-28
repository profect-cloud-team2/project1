package com.example.demo.store.service.ai;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiClient {

	private final WebClient webClient;

	public OpenAiClient() {
		Dotenv dotenv = Dotenv.load();
		String apiKey = dotenv.get("OPENAI_API_KEY");
		this.webClient = WebClient.builder()
			.baseUrl("https://api.openai.com/v1")
			.defaultHeader("Authorization", "Bearer " + apiKey)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}

	public String getCompletion(String prompt) {
		System.out.println("📤 Prompt: " + prompt);

		Map<String, Object> requestBody = Map.of(
			"model", "gpt-4o",
			"messages", new Object[] {
				Map.of("role", "system", "content", "당신은 고객의 시선을 끌 수 있는 참신하고 매력적인 가게 소개 문구를 작성하는 마케팅 전문가입니다. " +
					"소개는 50자 이내로 간결하면서도 눈길을 끌 수 있어야 합니다."),
				Map.of("role", "user", "content", prompt)
			},
			"temperature", 0.7
		);

		try {
			Map<String, Object> response = webClient.post()
				.uri("/chat/completions")
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				.block();

			// 응답 구조 출력 (디버깅용)
			System.out.println("🔎 Raw GPT 응답: " + response);

			if (response == null || !response.containsKey("choices")) {
				System.err.println("❌ GPT 응답이 비어 있음");
				return "AI 응답 없음";
			}

			List<?> choices = (List<?>)response.get("choices");
			if (choices.isEmpty()) {
				return "GPT 응답 없음";
			}

			Map<?, ?> firstChoice = (Map<?, ?>)choices.get(0);
			Map<?, ?> message = (Map<?, ?>)firstChoice.get("message");

			if (message == null || !message.containsKey("content")) {
				return "GPT 응답 내용 없음";
			}

			String content = message.get("content").toString();
			System.out.println("📥 GPT 응답: " + content);

			return content.trim();
		} catch (Exception e) {
			System.err.println("❌ GPT 호출 실패: " + e.getMessage());
			return "AI 설명 생성 실패";
		}
	}
}
