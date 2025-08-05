package com.example.demo.global.config;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@Component
public class DotenvLoader {

	@PostConstruct
	public void loadEnv() {
		Dotenv dotenv = Dotenv.configure()
			.directory(".")
			.ignoreIfMalformed()
			.ignoreIfMissing()
			.load();

		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});
	}

	@PostConstruct
	public void debugEnv() {
		String jwtSecret = System.getProperty("JWT_ACCESS_SECRET");
	}
}
