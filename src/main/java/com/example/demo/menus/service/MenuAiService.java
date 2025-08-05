package com.example.demo.menus.service;

import com.example.demo.menus.dto.MenuAiResponseDto;
import com.example.demo.menus.service.ai.MenuOpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuAiService {

    private final MenuOpenAiClient menuOpenAiClient;

    public MenuAiResponseDto generateMenuIntroduction(String name, String introduction) {
        // 프롬프트 생성
        String prompt = String.format(
                "메뉴 이름: %s, 현재 소개: %s\n" +
                        "50자 이내로 고객의 시선을 끌 수 있는 참신하고 매력적인 소개 문구를 작성해 주세요.",
                name,
                introduction == null ? "" : introduction
        );

        // OpenAI API 호출
        String rawResponse = menuOpenAiClient.getCompletion(prompt);
        String answerOnly = rawResponse.trim();

        // MenuAiResponseDto로 반환
        return new MenuAiResponseDto(prompt, answerOnly);
    }
}
