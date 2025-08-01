package com.example.demo.favorite.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.example.demo.store.entity.StoreEntity;
import com.example.demo.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteEntity {
	@Id  // PK: favorite_id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "favorite_id", updatable = false, nullable = false, columnDefinition = "UUID")
	private UUID favoriteId; // ← Hibernate 가 이 필드를 null일 때 채워줍니다.

	@ManyToOne(fetch = FetchType.LAZY)  // FK: user_id
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)  // FK: store_id
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity store;

	// @Column(name = "created_at")
	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", nullable = false)
	private UUID createdBy;

	// @Column(name = "updated_at")
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	// @Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	private UUID deletedBy;
}
