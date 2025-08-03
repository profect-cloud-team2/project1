package com.example.demo.admin.dto;

import com.example.demo.admin.entity.AdminReport;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AdminReportResponseDto {
	private UUID reportId;
	private String reportType;
	private String content;
	private String status;
	private LocalDateTime createdAt;

	private UserInfoDto reporter;
	private UserInfoDto reported;

	@Getter
	@Builder
	public static class UserInfoDto {
		private UUID userId;
		private String nickname;
		private String email;
	}

	public static AdminReportResponseDto fromEntity(AdminReport entity) {
		UserInfoDto reporter = null;
		if (entity.getReporter() != null) {
			reporter = UserInfoDto.builder()
				.userId(entity.getReporter().getUserId())
				.nickname(entity.getReporter().getNickname())
				.email(entity.getReporter().getEmail())
				.build();
		}

		UserInfoDto reported = null;
		if (entity.getReported() != null) {
			reported = UserInfoDto.builder()
				.userId(entity.getReported().getUserId())
				.nickname(entity.getReported().getNickname())
				.email(entity.getReported().getEmail())
				.build();
		}

		return AdminReportResponseDto.builder()
			.reportId(entity.getReportId())
			.reportType(entity.getReportType())
			.content(entity.getContent())
			.status(entity.getStatus().getDescription())
			.createdAt(entity.getCreatedAt())
			.reporter(reporter)
			.reported(reported)
			.build();
	}

}
