package com.example.demo.cart;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.dto.CartRes;
import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.entity.CartItemEntity;
import com.example.demo.cart.exception.CartNotFoundException;
import com.example.demo.cart.exception.InvalidQuantityException;
import com.example.demo.cart.repository.CartItemRepository;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.cart.service.CartServiceImpl;
import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CartServiceImpl cartService;

	@Test
	@DisplayName("새 장바구니에 아이템 추가 성공")
	void addItemToCart_NewCart_Success() {
		UUID userId = UUID.randomUUID();
		UUID menuId = UUID.randomUUID();
		UUID storeId = UUID.randomUUID();

		CartItemAddReq req = new CartItemAddReq();
		req.setStoreId(storeId);
		CartItemAddReq.MenuItem menuItem = new CartItemAddReq.MenuItem();
		menuItem.setMenuId(menuId);
		menuItem.setQuantity(2);
		req.setMenuItems(List.of(menuItem));

		UserEntity mockUser = mock(UserEntity.class);
		MenuEntity mockMenu = mock(MenuEntity.class);
		when(mockMenu.getPrice()).thenReturn(5000);

		given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.empty());
		given(menuRepository.findById(menuId)).willReturn(Optional.of(mockMenu));
		given(cartRepository.save(any(CartEntity.class))).willAnswer(invocation -> {
			CartEntity cart = invocation.getArgument(0);
			cart.setCartId(UUID.randomUUID());
			return cart;
		});
		given(cartItemRepository.findByCartCartIdAndMenuMenuId(any(), eq(menuId))).willReturn(Optional.empty());

		cartService.addItemToCart(req, userId);

		verify(cartRepository).save(any(CartEntity.class));
		verify(cartItemRepository).save(any(CartItemEntity.class));
	}

	@Test
	@DisplayName("기존 장바구니에 아이템 추가 성공")
	void addItemToCart_ExistingCart_Success() {
		UUID userId = UUID.randomUUID();
		UUID menuId = UUID.randomUUID();
		UUID storeId = UUID.randomUUID();

		CartItemAddReq req = new CartItemAddReq();
		req.setStoreId(storeId);
		CartItemAddReq.MenuItem menuItem = new CartItemAddReq.MenuItem();
		menuItem.setMenuId(menuId);
		menuItem.setQuantity(1);
		req.setMenuItems(List.of(menuItem));

		UserEntity mockUser = mock(UserEntity.class);
		MenuEntity mockMenu = mock(MenuEntity.class);
		when(mockMenu.getPrice()).thenReturn(5000);

		CartEntity existingCart = mock(CartEntity.class);
		CartItemEntity mockItem = mock(CartItemEntity.class);
		MenuEntity existingMenu = mock(MenuEntity.class);
		StoreEntity existingStore = mock(StoreEntity.class);
		when(existingStore.getStoreId()).thenReturn(storeId);
		when(existingMenu.getStore()).thenReturn(existingStore);
		when(mockItem.getMenu()).thenReturn(existingMenu);
		when(existingCart.getItems()).thenReturn(List.of(mockItem));

		given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.of(existingCart));
		given(menuRepository.findById(menuId)).willReturn(Optional.of(mockMenu));
		given(cartItemRepository.findByCartCartIdAndMenuMenuId(any(), eq(menuId))).willReturn(Optional.empty());

		cartService.addItemToCart(req, userId);

		verify(cartItemRepository).save(any(CartItemEntity.class));
	}

	@Test
	@DisplayName("동일 메뉴 수량 증가 성공")
	void addItemToCart_SameMenu_IncreaseQuantity() {
		UUID userId = UUID.randomUUID();
		UUID menuId = UUID.randomUUID();
		UUID storeId = UUID.randomUUID();

		CartItemAddReq req = new CartItemAddReq();
		req.setStoreId(storeId);
		CartItemAddReq.MenuItem menuItem = new CartItemAddReq.MenuItem();
		menuItem.setMenuId(menuId);
		menuItem.setQuantity(2);
		req.setMenuItems(List.of(menuItem));

		UserEntity mockUser = mock(UserEntity.class);
		MenuEntity mockMenu = mock(MenuEntity.class);
		when(mockMenu.getPrice()).thenReturn(5000);

		CartEntity existingCart = mock(CartEntity.class);
		CartItemEntity existingItem = mock(CartItemEntity.class);
		when(existingItem.getQuantity()).thenReturn(1);

		given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.of(existingCart));
		given(menuRepository.findById(menuId)).willReturn(Optional.of(mockMenu));
		given(cartItemRepository.findByCartCartIdAndMenuMenuId(any(), eq(menuId))).willReturn(Optional.of(existingItem));

		cartService.addItemToCart(req, userId);

		verify(existingItem).setQuantity(3);
		verify(cartItemRepository).save(existingItem);
	}

	@Test
	@DisplayName("다른 가게 메뉴 추가 실패")
	void addItemToCart_DifferentStore_ThrowsException() {
		UUID userId = UUID.randomUUID();
		UUID menuId = UUID.randomUUID();
		UUID storeId1 = UUID.randomUUID();
		UUID storeId2 = UUID.randomUUID();

		CartItemAddReq req = new CartItemAddReq();
		req.setStoreId(storeId2);
		CartItemAddReq.MenuItem menuItem = new CartItemAddReq.MenuItem();
		menuItem.setMenuId(menuId);
		menuItem.setQuantity(1);
		req.setMenuItems(List.of(menuItem));

		UserEntity mockUser = mock(UserEntity.class);
		CartEntity existingCart = mock(CartEntity.class);
		CartItemEntity mockItem = mock(CartItemEntity.class);
		MenuEntity existingMenu = mock(MenuEntity.class);
		StoreEntity existingStore = mock(StoreEntity.class);
		when(existingStore.getStoreId()).thenReturn(storeId1);
		when(existingMenu.getStore()).thenReturn(existingStore);
		when(mockItem.getMenu()).thenReturn(existingMenu);
		when(existingCart.getItems()).thenReturn(List.of(mockItem));

		given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.of(existingCart));

		assertThatThrownBy(() -> cartService.addItemToCart(req, userId))
			.isInstanceOf(InvalidQuantityException.class);
	}

	@Test
	@DisplayName("존재하지 않는 사용자 실패")
	void addItemToCart_UserNotFound_ThrowsException() {
		UUID userId = UUID.randomUUID();
		UUID menuId = UUID.randomUUID();
		UUID storeId = UUID.randomUUID();

		CartItemAddReq req = new CartItemAddReq();
		req.setStoreId(storeId);
		CartItemAddReq.MenuItem menuItem = new CartItemAddReq.MenuItem();
		menuItem.setMenuId(menuId);
		menuItem.setQuantity(1);
		req.setMenuItems(List.of(menuItem));

		given(userRepository.findById(userId)).willReturn(Optional.empty());

		assertThatThrownBy(() -> cartService.addItemToCart(req, userId))
			.isInstanceOf(CartNotFoundException.class);
	}

	@Test
	@DisplayName("내 장바구니 조회 성공")
	void getMyCart_Success() {
		UUID userId = UUID.randomUUID();
		UUID cartId = UUID.randomUUID();
		UserEntity mockUser = mock(UserEntity.class);
		CartEntity mockCart = mock(CartEntity.class);
		when(mockCart.getCartId()).thenReturn(cartId);
		when(mockCart.getItems()).thenReturn(new ArrayList<>());

		given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
		given(cartRepository.findWithItemsByUserId(userId)).willReturn(Optional.of(mockCart));

		List<CartRes> result = cartService.getMyCart(userId);

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getCartId()).isEqualTo(cartId);
	}

	@Test
	@DisplayName("수량 변경 성공")
	void updateQuantity_Success() {
		UUID cartItemId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		int newQuantity = 3;

		CartItemEntity mockItem = mock(CartItemEntity.class);
		given(cartItemRepository.findById(cartItemId)).willReturn(Optional.of(mockItem));

		cartService.updateQuantity(cartItemId, newQuantity, userId);

		verify(mockItem).setQuantity(newQuantity);
		verify(mockItem).setUpdatedAt(any(LocalDateTime.class));
		verify(mockItem).setUpdatedBy(userId);
	}

	@Test
	@DisplayName("잘못된 수량 입력 실패")
	void updateQuantity_InvalidQuantity_ThrowsException() {
		UUID cartItemId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		int invalidQuantity = 0;

		assertThatThrownBy(() -> cartService.updateQuantity(cartItemId, invalidQuantity, userId))
			.isInstanceOf(InvalidQuantityException.class);
	}

	@Test
	@DisplayName("아이템 삭제 성공")
	void deleteItem_Success() {
		UUID cartItemId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();

		CartItemEntity mockItem = mock(CartItemEntity.class);
		given(cartItemRepository.findById(cartItemId)).willReturn(Optional.of(mockItem));

		cartService.deleteItem(cartItemId, userId);

		verify(cartItemRepository).deleteById(cartItemId);
	}
}