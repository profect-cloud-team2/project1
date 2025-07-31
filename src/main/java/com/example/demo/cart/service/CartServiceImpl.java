package com.example.demo.cart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.cart.dto.CartCreateReq;
import com.example.demo.cart.dto.CartItemRes;
import com.example.demo.cart.dto.CartRes;
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
	public UUID CreateCart(CartCreateReq req) {
		UUID userId = getCurrentUserId();

		UserEntity user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

		StoreEntity store = storeRepository.findById(req.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("가게 없음"));

		Optional<CartEntity> existing = cartRepository
			.findByUser_UserIdAndStore_StoreIdAndDeletedAtIsNull(userId, req.getStoreId());

		if (existing.isPresent()) {
			return existing.get().getCartId();
		}

		CartEntity cart = new CartEntity();
		cart.setUserId(user);
		cart.setStoreId(store);
		cart.setCreatedAt(LocalDateTime.now());
		cart.setCreatedBy(userId);

		return cartRepository.save(cart).getCartId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartRes> getMyCart() {
		UUID userId = getCurrentUserId();

		List<CartEntity> carts = cartRepository.findAllByUser_UserIdAndDeletedAtIsNull(userId);

		return carts.stream().map(cart -> {
			CartRes res = new CartRes();
			res.setCartId(cart.getCartId());
			res.setStoreId(cart.getStoreId().getStoreId());

			List<CartItemRes> itemRes = cart.getItems().stream().map(item -> {
				CartItemRes itemDto = new CartItemRes();
				itemDto.setMenuId(item.getMenu().getId());
				itemDto.setMenuName(item.getMenu().getName());
				itemDto.setQuantity(item.getQuantity());
				itemDto.setPrice(item.getPrice());
				return itemDto;
			}).toList();

			res.setItems(itemRes);
			return res;
		}).toList();
	}

	private UUID getCurrentUserId() {
		String userIdStr = SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getName();
		return UUID.fromString(userIdStr);
	}
}
