package com.example.demo.cart.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.menus.entity.MenuEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "p_cart_item")
public class CartItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cart_item_id")
	private UUID cartItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private CartEntity cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private MenuEntity menu;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private int price;

	private LocalDateTime createdAt;
	private UUID createdBy;

	private LocalDateTime updatedAt;
	private UUID updatedBy;
}
