package com.example.demo.admin.entity;

import java.util.UUID;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.demo.user.entity.UserEntity;

@Entity
@Data
@Table(name = "p_admin_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReport {

	@Id
	@GeneratedValue
	@Column(name = "report_id")
	private UUID reportId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_id", nullable = false)
	private UserEntity reporter; // 신고자 (유저 or 점주), p_user_info 참조

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_id")
	private UserEntity reported;

	@Column(name = "report_type", nullable = false)
	private String reportType; // USER / STORE / REVIEW

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ReportStatus status;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

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
}
