package com.example.demo.order.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateReq {
	private UUID storeId;
	private String requestMessage;
	private List<UUID> cartItemIds; // 장바구니 아이템 ID 목록
}
