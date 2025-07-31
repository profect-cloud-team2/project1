package com.example.demo.favorite.entity;

import java.time.LocalDateTime;
import java.util.UUID;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_favorite")
@Getter
@NoArgsConstructor

public class FavoriteEntity {
	@Id  // PK: favorite_id
	@GeneratedValue
	@Column(name = "favorite_id", columnDefinition = "UUID")
	private UUID favoriteId;

	@ManyToOne(fetch = FetchType.LAZY)  // FK: user_id
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)  // FK: store_id
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity store;

	// @Column(name = "created_at")
	private LocalDateTime createdAt;
	private UUID createdBy;

	// @Column(name = "updated_at")
	private LocalDateTime updatedAt;
	private UUID updatedBy;

	// @Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	private UUID deletedBy;
}
