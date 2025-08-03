package com.example.demo.cart;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartRepositoryTest {

	@Mock
	private CartRepository cartRepository;

	@Test
	@DisplayName("아이템과 함께 장바구니 조회")
	void findWithItemsByUserId() {
		UUID userId = UUID.randomUUID();
		CartEntity cart = new CartEntity();
		cart.setCartId(UUID.randomUUID());
		cart.setCreatedAt(LocalDateTime.now());
		cart.setCreatedBy(userId);

		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.of(cart));

		Optional<CartEntity> result = cartRepository.findWithItemsByUserId(userId);

		assertThat(result).isPresent();
	}

	@Test
	@DisplayName("모든 장바구니 조회")
	void findAll() {
		CartEntity cart = new CartEntity();
		cart.setCartId(UUID.randomUUID());
		cart.setCreatedAt(LocalDateTime.now());
		cart.setCreatedBy(UUID.randomUUID());

		given(cartRepository.findAll()).willReturn(Arrays.asList(cart));

		List<CartEntity> result = cartRepository.findAll();

		assertThat(result).hasSize(1);
	}
}
