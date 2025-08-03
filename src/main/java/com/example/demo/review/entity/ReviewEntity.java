package com.example.demo.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "review_id", nullable = false)
	private UUID reviewId;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "store_id", nullable = false)
	private UUID storeId;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Column(name = "rating", nullable = false)
	private Integer rating = 1;

	@Column(name = "content")
	private String content;

	@Column(name = "imgURL")
	private String imgURL;

	@Column(name = "owner_Review")
	private String ownerReview;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", nullable = false)
	private UUID createdBy;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "updated_by")
	private UUID updatedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "deleted_by")
	private UUID deletedBy;
}
