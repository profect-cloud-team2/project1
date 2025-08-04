package com.example.demo.ai.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.ai.entity.AiEntity;

@Repository
public interface AiRepository extends JpaRepository<AiEntity, UUID> {
	/**
	 * 전체 AI 로그를 조회합니다.
	 * (findAll() 은 JpaRepository 에서 기본 제공되므로
	 * 선언하지 않아도 바로 사용 가능합니다.)
	 */
	@Override
	List<AiEntity> findAll();

	/**
	 * apiType 으로 AI 로그를 조회합니다.
	 * 예를 들어 "STORE_DESC", "MENU_DESC" 등으로 필터링.
	 */
	List<AiEntity> findByApiType(String apiType);
}
