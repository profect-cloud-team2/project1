// package com.example.demo.cart;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.doNothing;
//
// import java.util.Arrays;
// import java.util.List;
// import java.util.UUID;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import com.example.demo.cart.dto.CartItemAddReq;
// import com.example.demo.cart.service.CartService;
//
// @ExtendWith(MockitoExtension.class)
// class CartServiceTest {
//
// 	@Mock
// 	private CartService cartService;
//
// 	@Test
// 	@DisplayName("장바구니에 메뉴 추가 성공")
// 	void addItemToCart_Success() {
// 		CartItemAddReq request = new CartItemAddReq();
// 		request.setStoreId(UUID.randomUUID());
//
// 		CartItemAddReq.MenuItem menuItem = new CartItemAddReq.MenuItem();
// 		menuItem.setMenuId(UUID.randomUUID());
// 		menuItem.setQuantity(2);
// 		menuItem.setPrice(5000);
// 		request.setMenuItems(Arrays.asList(menuItem));
//
// 		doNothing().when(cartService).addItemToCart(any(CartItemAddReq.class));
//
// 		cartService.addItemToCart(request);
// 	}
//
// 	@Test
// 	@DisplayName("내 장바구니 조회 성공")
// 	void getMyCart_Success() {
// 		given(cartService.getMyCart()).willReturn(Arrays.asList());
//
// 		List result = cartService.getMyCart();
//
// 		assertThat(result).isNotNull();
// 	}
//
// 	@Test
// 	@DisplayName("장바구니 아이템 수량 수정 성공")
// 	void updateQuantity_Success() {
// 		UUID cartItemId = UUID.randomUUID();
// 		int quantity = 3;
//
// 		doNothing().when(cartService).updateQuantity(cartItemId, quantity);
//
// 		cartService.updateQuantity(cartItemId, quantity);
// 	}
//
// 	@Test
// 	@DisplayName("장바구니 아이템 삭제 성공")
// 	void deleteItem_Success() {
// 		UUID cartItemId = UUID.randomUUID();
//
// 		doNothing().when(cartService).deleteItem(cartItemId);
//
// 		cartService.deleteItem(cartItemId);
// 	}
// }
