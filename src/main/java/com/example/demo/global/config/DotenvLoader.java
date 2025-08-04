package com.example.demo.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DotenvLoader {

	@PostConstruct
	public void loadEnv() {
		Dotenv dotenv = Dotenv.configure()
			.directory(".") // 루트에 있는 .env
			.ignoreIfMalformed()
			.ignoreIfMissing()
			.load();

		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});
	}

	// ✅ .env 로딩 후 환경변수 제대로 들어왔는지 확인
	@PostConstruct
	public void debugEnv() {
		String jwtSecret = System.getProperty("JWT_ACCESS_SECRET");
		System.out.println("✅ JWT_ACCESS_SECRET = " + jwtSecret);
	}
}
