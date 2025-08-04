package com.example.demo.cart;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.user.entity.UserEntity;

@ExtendWith(MockitoExtension.class)
class CartRepositoryTest {

	@Mock
	private CartRepository cartRepository;

	@Test
	@DisplayName("사용자별 장바구니 조회 성공")
	void findWithItemsByUserId_Success() {
		UUID userId = UUID.randomUUID();
		UserEntity mockUser = mock(UserEntity.class);
		when(mockUser.getUserId()).thenReturn(userId);
		
		CartEntity cart = new CartEntity();
		cart.setUser(mockUser);

		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.of(cart));

		Optional<CartEntity> result = cartRepository.findWithItemsByUserId(userId);

		assertThat(result).isPresent();
		assertThat(result.get().getUser().getUserId()).isEqualTo(userId);
	}

	@Test
	@DisplayName("존재하지 않는 사용자 장바구니 조회")
	void findWithItemsByUserId_NotFound() {
		UUID nonExistentUserId = UUID.randomUUID();

		given(cartRepository.findWithItemsByUserId(nonExistentUserId)).willReturn(Optional.empty());

		Optional<CartEntity> result = cartRepository.findWithItemsByUserId(nonExistentUserId);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("새 장바구니 생성 성공")
	void save_NewCart_Success() {
		UUID userId = UUID.randomUUID();
		UserEntity mockUser = mock(UserEntity.class);
		when(mockUser.getUserId()).thenReturn(userId);
		
		CartEntity cart = new CartEntity();
		cart.setUser(mockUser);
		
		CartEntity savedCart = new CartEntity();
		savedCart.setCartId(UUID.randomUUID());
		savedCart.setUser(mockUser);

		when(cartRepository.save(cart)).thenReturn(savedCart);

		CartEntity result = cartRepository.save(cart);

		assertThat(result.getCartId()).isNotNull();
		assertThat(result.getUser().getUserId()).isEqualTo(userId);
		verify(cartRepository).save(cart);
	}

	@Test
	@DisplayName("기존 장바구니 업데이트 성공")
	void save_ExistingCart_Success() {
		UUID userId = UUID.randomUUID();
		UUID cartId = UUID.randomUUID();
		
		CartEntity existingCart = new CartEntity();
		existingCart.setCartId(cartId);
		existingCart.setUpdatedAt(LocalDateTime.now());
		existingCart.setUpdatedBy(userId);

		when(cartRepository.save(existingCart)).thenReturn(existingCart);

		CartEntity result = cartRepository.save(existingCart);

		assertThat(result.getCartId()).isEqualTo(cartId);
		assertThat(result.getUpdatedAt()).isNotNull();
		assertThat(result.getUpdatedBy()).isEqualTo(userId);
		verify(cartRepository).save(existingCart);
	}

	@Test
	@DisplayName("장바구니 ID로 조회 성공")
	void findById_Success() {
		UUID cartId = UUID.randomUUID();
		
		CartEntity cart = new CartEntity();
		cart.setCartId(cartId);

		given(cartRepository.findById(cartId)).willReturn(Optional.of(cart));

		Optional<CartEntity> result = cartRepository.findById(cartId);

		assertThat(result).isPresent();
		assertThat(result.get().getCartId()).isEqualTo(cartId);
	}

	@Test
	@DisplayName("존재하지 않는 장바구니 ID로 조회")
	void findById_NotFound() {
		UUID nonExistentCartId = UUID.randomUUID();

		given(cartRepository.findById(nonExistentCartId)).willReturn(Optional.empty());

		Optional<CartEntity> result = cartRepository.findById(nonExistentCartId);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("장바구니 삭제 성공")
	void delete_Success() {
		CartEntity cart = new CartEntity();

		cartRepository.delete(cart);

		verify(cartRepository).delete(cart);
	}
}