package com.example.demo.store.service.ai;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiClient {

	private final RestTemplate restTemplate;
	private final String apiKey;

	public OpenAiClient() {
		this.restTemplate = new RestTemplate();
		Dotenv dotenv = Dotenv.load();
		this.apiKey = dotenv.get("OPENAI_API_KEY");
	}

	public String getCompletion(String prompt) {
		System.out.println("📤 Prompt: " + prompt);

		String url = "https://api.openai.com/v1/chat/completions";

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(apiKey);

		// 요청 바디 설정
		Map<String, Object> requestBody = Map.of(
			"model", "gpt-4o",
			"messages", List.of(
				Map.of("role", "system", "content", "당신은 고객의 시선을 끌 수 있는 참신하고 매력적인 가게 소개 문구를 작성하는 마케팅 전문가입니다. " +
					"소개는 50자 이내로 간결하면서도 눈길을 끌 수 있어야 합니다."),
				Map.of("role", "user", "content", prompt)
			),
			"temperature", 0.7
		);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				httpEntity,
				new ParameterizedTypeReference<Map<String, Object>>() {}
			);
			Map<String, Object> responseBody = response.getBody();

			System.out.println("🔎 Raw GPT 응답: " + responseBody);

			if (responseBody == null || !responseBody.containsKey("choices")) {
				System.err.println("❌ GPT 응답이 비어 있음");
				return "AI 응답 없음";
			}

			List<?> choices = (List<?>) responseBody.get("choices");
			if (choices.isEmpty()) {
				return "GPT 응답 없음";
			}

			Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
			Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");

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
