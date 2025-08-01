package com.example.demo.menus.service.ai;

import com.example.demo.menus.dto.MenuIntroductionRequestDto;
import com.example.demo.menus.dto.MenuIntroductionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MenuOpenAiClient {
    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateIntroduction(MenuIntroductionRequestDto request) {
        String prompt = String.format(
                "메뉴 이름: %s\n설명: %s\n이 메뉴에 대해 고객이 식욕을 느낄 수 있도록 간단하고 매력적인 소개 문장을 작성해줘.",
                request.getName(), request.getIntroduction()
        );

        // 요청 Body 구성
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", prompt)
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<MenuIntroductionResponseDto> response =
                restTemplate.exchange("https://api.openai.com/v1/chat/completions",
                        HttpMethod.POST, entity, MenuIntroductionResponseDto.class);

        return response.getBody() != null && !response.getBody().getChoices().isEmpty()
                ? response.getBody().getChoices().get(0).getMessage().getContent()
                : "소개를 생성할 수 없습니다.";
    }
}
