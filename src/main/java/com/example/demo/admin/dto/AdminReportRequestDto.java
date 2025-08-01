package com.example.demo.admin.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminReportRequestDto {
	private UUID reportedId;     // 피신고 대상 ID
	private String reportType;   // USER, STORE, REVIEW
	private String content;      // 신고 내용
}
