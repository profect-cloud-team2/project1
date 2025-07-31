package com.example.demo.cart.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.cart.dto.CartAddReq;
import com.example.demo.cart.dto.CartAddRes;
import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	@Transactional
	@Override
	public CartAddRes addCart(CartAddReq req) {
		UUID userId = getCurrentUserId();

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		StoreEntity store = storeRepository.findById(req.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

		CartEntity cart = new CartEntity();
		cart.setCartId(UUID.randomUUID());
		cart.setUserId(user);
		cart.setStoreId(store);
		cart.setCreatedAt(LocalDateTime.now());
		cart.setCreatedBy(userId);

		CartEntity saved = cartRepository.save(cart);
		return new CartAddRes("장바구니가 생성되었습니다.");
	}

	private UUID getCurrentUserId() {
		String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
		return UUID.fromString(userIdStr);
	}
}
