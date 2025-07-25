package com.example.demo.store.service.ai;

import org.springframework.stereotype.Component;

@Component
public class OpenAiClient {

	public String getCompletion(String prompt) {
		// 실제 OpenAI 호출은 배포 전까지 생략
		return "김밥천국 대표도 인정한 지점입니다.";
	}
}
