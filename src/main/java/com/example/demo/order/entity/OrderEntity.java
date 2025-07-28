package com.example.demo.order.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.store.entity.StoreEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "p_order_info")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private UUID orderId;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "user_id", nullable = false)
	// private UserEntity userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity storeId;

	@Column(name = "total_price", nullable = false)
	private int total_price;

	@Column(name = "requestMessage")
	private String requestMessage;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus;

	private LocalDateTime createdAt;
	private UUID createBy;

	private LocalDateTime updatedAt;
	private UUID updateBy;

	private LocalDateTime deletedAt;
	private UUID deletedBy;
}
