package com.example.demo.admin.dto;

import com.example.demo.admin.entity.AdminUserSanction;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AdminUserSanctionResponseDto {

	private UUID sanctionId;
	private UUID userId;
	private String reason;
	private String sanctionStatus;
	private String note;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime createdAt;
	private UUID createdBy;
	private LocalDateTime updatedAt;
	private UUID updatedBy;
	private LocalDateTime deletedAt;
	private UUID deletedBy;

	public static AdminUserSanctionResponseDto from(AdminUserSanction sanction) {
		AdminUserSanctionResponseDto dto = new AdminUserSanctionResponseDto();
		dto.sanctionId = sanction.getSanctionId();
		dto.userId = sanction.getUserId();
		dto.reason = sanction.getReason();
		dto.sanctionStatus = sanction.getSanctionStatus().getDisplayName();
		dto.note = sanction.getNote();
		dto.startDate = sanction.getStartDate();
		dto.endDate = sanction.getEndDate();
		dto.createdAt = sanction.getCreatedAt();
		dto.createdBy = sanction.getCreatedBy();
		dto.updatedAt = sanction.getUpdatedAt();
		dto.updatedBy = sanction.getUpdatedBy();
		dto.deletedAt = sanction.getDeletedAt();
		dto.deletedBy = sanction.getDeletedBy();
		return dto;
	}
}
