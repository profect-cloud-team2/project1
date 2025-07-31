package com.example.demo.cart.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.store.entity.StoreEntity;
import com.example.demo.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Setter;

@Entity
@Setter
@Table(name = "p_cart")
public class CartEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cart_id")
	private UUID cartId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity storeId;

	private LocalDateTime createdAt;
	private UUID createdBy;

	private LocalDateTime updatedAt;
	private UUID updateBy;

	private LocalDateTime deletedAt;
	private UUID deletedBy;
}
