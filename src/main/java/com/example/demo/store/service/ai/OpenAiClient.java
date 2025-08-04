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
		String url = "https://api.openai.com/v1/chat/completions";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(apiKey);

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
				new ParameterizedTypeReference<>() {}
			);

			List<?> choices = (List<?>) response.getBody().get("choices");
			Map<?, ?> message = (Map<?, ?>) ((Map<?, ?>) choices.get(0)).get("message");
			String content = message.get("content").toString();

			return content.trim();
		} catch (Exception e) {
			return "";
		}
	}
}
