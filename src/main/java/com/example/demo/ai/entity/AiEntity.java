package com.example.demo.ai.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_ai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "log_id", updatable = false, nullable = false, columnDefinition = "UUID")
	private UUID logId;

	/**
	 * ai API 용도 구분.
	 * STORE_DESC(가게설명생성), MENU_DESC(메뉴설명생성)
	 */
	@Column(name = "api_type", nullable = false, length = 50)
	private String apiType;

	/**
	 * AI 호출 시 보낸 Request 전체 JSON (TEXT 타입)
	 */
	@Column(name = "request_json", columnDefinition = "TEXT", nullable = false)
	private String requestJson;

	/**
	 * AI 호출 후 받은 Response 전체 JSON 또는 텍스트 (TEXT 타입)
	 */
	@Column(name = "response_json", columnDefinition = "TEXT", nullable = false)
	private String responseJson;

	/**
	 * 생성일자 (DB default CURRENT_TIMESTAMP)
	 */
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", nullable = false)
	private UUID createdBy;
}
