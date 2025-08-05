package com.example.demo.admin.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminReportRequestDto {
	private UUID reportedId;
	private String reportType;
	private String content;
}
