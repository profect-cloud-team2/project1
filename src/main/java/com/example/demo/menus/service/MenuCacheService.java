package com.example.demo.menus.service;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.menus.dto.MenuResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuCacheService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper; // ObjectMapper 주입 필요
	private static final String MENU_KEY_PREFIX = "menu::store::";

	public void cacheMenus(String storeId, List<MenuResponseDto> menus) {
		redisTemplate.opsForValue().set(MENU_KEY_PREFIX + storeId, menus, Duration.ofMinutes(10));
		log.info("✅ Redis에 메뉴 저장됨: key = {}", MENU_KEY_PREFIX + storeId); // 👈 이 줄 추가
	}

	public List<MenuResponseDto> getCachedMenus(String storeId) {
		Object cached = redisTemplate.opsForValue().get(MENU_KEY_PREFIX + storeId);
		if (cached == null) return null;

		// 💡 JSON을 명시적 타입으로 안전하게 역직렬화
		return objectMapper.convertValue(
			cached,
			new TypeReference<List<MenuResponseDto>>() {}
		);
	}

	public void evictMenus(String storeId) {
		redisTemplate.delete(MENU_KEY_PREFIX + storeId);
	}
}

