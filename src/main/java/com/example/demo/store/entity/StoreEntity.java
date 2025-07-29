package com.example.demo.store.entity;

import com.example.demo.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "p_store_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID storeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "businessNum", nullable = false, length = 10)
	private String businessNum;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private Category category;

	@Column(name = "address1", nullable = false, length = 255)
	private String address1;

	@Column(name = "address1", nullable = false, length = 255)
	private String address2;

	@Column(precision = 9, scale = 6)
	private BigDecimal store_latitude;

	@Column(precision = 9, scale = 6)
	private BigDecimal store_longitude;

	@Column(name = "phoneNum",nullable = false, length = 10)
	private String phoneNum;

	@Column(name = "introduction", nullable = true)
	private String introduction;

	@Column(name = "imgURL", nullable = true, length = 255)
	private String imgURL;

	@Column(name = "openTime", nullable = false)
	private Integer openTime;

	@Column(name = "closedTime", nullable = false)
	private String closedTime;

	@Column(name = "aiDescription")
	private String aiDescription;

	@Column(name="totalProfit")
	private Float totalProfit;

	@Column(name = "orderCount")
	private Float orderCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "isAvailable", nullable = false)
	private StoreStatus isAvailable;

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

	// 생성용 entity에 맞춰 수정 필요
	public static StoreEntity fromCreateDto(com.example.demo.store.dto.StoreCreateRequestDto dto, String aiDescription) {
		StoreEntity entity = new StoreEntity();
		entity.name = dto.getName();
		entity.businessNum = dto.getBusinessNum();
		entity.user = UserEntity.builder().build();
		entity.category = dto.getCategory();
		entity.address1 = dto.getAddress1();
		entity.address2 = dto.getAddress2();
		entity.phoneNum = dto.getPhoneNum();
		entity.imgURL = dto.getImgURL();
		entity.openTime = dto.getOpenTime();
		entity.closedTime = dto.getClosedTime();
		entity.aiDescription = aiDescription;
		return entity;
	}

	// 수정용 entity에 맞춰 수정 필요
	public void updateFromDto(com.example.demo.store.dto.StoreUpdateRequestDto dto) {
		this.name = dto.getName();
		this.category = dto.getCategory();
		this.address1 = dto.getAddress1();
		this.address2 = dto.getAddress2();
		this.phoneNum = dto.getPhoneNum();
		this.imgURL = dto.getImgURL();
		this.openTime = dto.getOpenTime();
		this.closedTime = dto.getClosedTime();
	}
}

