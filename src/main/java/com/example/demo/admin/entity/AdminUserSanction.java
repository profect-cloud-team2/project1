package com.example.demo.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "p_admin_user_sanctions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdminUserSanction {

	@Id
	@GeneratedValue
	@Column(name = "sanction_id")
	private UUID sanctionId;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "reason", nullable = false, columnDefinition = "TEXT")
	private String reason;

	@Enumerated(EnumType.STRING)
	@Column(name = "sanction_status", nullable = false)
	private SanctionStatus sanctionStatus;

	@Column(name = "note", columnDefinition = "TEXT")
	private String note;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "created_by", nullable = false)
	private UUID createdBy;

	@Column(name = "updated_by")
	private UUID updatedBy;

	@Column(name = "deleted_by")
	private UUID deletedBy;

	public void softDelete(UUID adminId) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = adminId;
	}
}
