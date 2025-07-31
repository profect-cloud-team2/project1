package com.example.demo.cart.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.repository.CartItemRepository;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final MenuRepository menuRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public void addItemToCart(CartItemAddReq req) {
		UUID userId = getCurrentUserId();

		CartEntity cart = cartRepository.findByUser_UserIdAndStore_StoreIdAndDeletedAtIsNull(userId, req.getStoreId())

	}

	private UUID getCurrentUserId() {
		String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
		return UUID.fromString(userIdStr);
	}
}
