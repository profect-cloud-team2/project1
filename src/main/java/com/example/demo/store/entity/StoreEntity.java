package com.example.demo.store.entity;

import com.example.demo.store.dto.StoreCreateRequestDto;
import com.example.demo.store.dto.StoreUpdateRequestDto;
import com.example.demo.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalTime;


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

	@Column(name = "business_num", nullable = false, length = 10)
	private String businessNum;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private Category category;

	@Column(name = "address1", nullable = false, length = 255)
	private String address1;

	@Column(name = "address2", nullable = false, length = 255)
	private String address2;

	@Column(precision = 9, scale = 6)
	private BigDecimal store_latitude;

	@Column(precision = 9, scale = 6)
	private BigDecimal store_longitude;

	@Column(name = "phone_num",nullable = false, length = 11)
	private String phoneNum;

	@Column(name = "introduction", nullable = true)
	private String introduction;

	@Column(name = "imgURL", nullable = true, length = 255)
	private String imgURL;

	@Column(name = "open_time", nullable = false)
	private LocalTime openTime;

	@Column(name = "closed_time", nullable = false)
	private LocalTime closedTime;

	@Column(name = "ai_description")
	private String aiDescription;

	@Column(name="total_profit")
	private Float totalProfit;

	@Column(name = "order_count")
	private Float orderCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "is_available", nullable = false)
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

	public static StoreEntity fromCreateDto(StoreCreateRequestDto dto, UserEntity user, String aiDescription) {
		StoreEntity entity = new StoreEntity();
		entity.user = user;
		entity.name = dto.getName();
		entity.businessNum = dto.getBusinessNum();
		entity.category = dto.getCategory();
		entity.address1 = dto.getAddress1();
		entity.address2 = dto.getAddress2();
		entity.store_latitude = dto.getStoreLatitude();
		entity.store_longitude = dto.getStoreLongitude();
		entity.phoneNum = dto.getPhoneNum();
		entity.introduction = dto.getIntroduction();
		entity.imgURL = dto.getImgURL();
		entity.openTime = LocalTime.parse(dto.getOpenTime());
		entity.closedTime = LocalTime.parse(dto.getClosedTime());
		entity.aiDescription = aiDescription;
		entity.totalProfit = 0.0f;
		entity.orderCount = 0.0f;
		entity.isAvailable = StoreStatus.OPEN; // 기본값 설정 필요 시
		// createdBy는 서비스 레이어에서 설정
		return entity;
	}

	public void updateFromDto(StoreUpdateRequestDto dto) {
		this.name = dto.getName();
		this.category = dto.getCategory();
		this.address1 = dto.getAddress1();
		this.address2 = dto.getAddress2();
		this.store_latitude = dto.getStoreLatitude();
		this.store_longitude = dto.getStoreLongitude();
		this.phoneNum = dto.getPhoneNum();
		this.introduction = dto.getIntroduction();
		this.imgURL = dto.getImgURL();
		this.openTime = LocalTime.parse(dto.getOpenTime());
		this.closedTime = LocalTime.parse(dto.getClosedTime());
		this.isAvailable = dto.getIsAvailable(); // Enum 타입이라면 그대로 매핑
	}
}

