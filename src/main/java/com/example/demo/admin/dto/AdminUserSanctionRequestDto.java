package com.example.demo.admin.dto;

import com.example.demo.admin.entity.SanctionStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class AdminUserSanctionRequestDto {
	private UUID userId;
	private String reason;
	private SanctionStatus sanctionStatus;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String note;
}
