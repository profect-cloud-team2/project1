// package com.example.demo.cart;
//
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.doNothing;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import java.util.Arrays;
// import java.util.UUID;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
// import com.example.demo.cart.controller.CartController;
// import com.example.demo.cart.dto.CartItemAddReq;
// import com.example.demo.cart.service.CartService;
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// @ExtendWith(MockitoExtension.class)
// class CartControllerTest {
//
// 	private MockMvc mockMvc;
// 	private ObjectMapper objectMapper;
//
// 	@Mock
// 	private CartService cartService;
//
// 	@BeforeEach
// 	void setUp() {
// 		CartController cartController = new CartController(cartService);
// 		mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
// 		objectMapper = new ObjectMapper();
// 	}
//
// 	@Test
// 	@DisplayName("장바구니에 메뉴 추가 성공")
// 	void addItemToCart_Success() throws Exception {
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
// 		mockMvc.perform(post("/api/cart/add")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(request)))
// 			.andExpect(status().isOk());
// 	}
//
// 	@Test
// 	@DisplayName("내 장바구니 조회 성공")
// 	void getMyCart_Success() throws Exception {
// 		given(cartService.getMyCart()).willReturn(Arrays.asList());
//
// 		mockMvc.perform(get("/api/cart"))
// 			.andExpect(status().isOk());
// 	}
//
// 	@Test
// 	@DisplayName("장바구니 아이템 수량 수정 성공")
// 	void updateQuantity_Success() throws Exception {
// 		UUID cartItemId = UUID.randomUUID();
// 		int quantity = 3;
//
// 		doNothing().when(cartService).updateQuantity(cartItemId, quantity);
//
// 		mockMvc.perform(patch("/api/cart/items/{cartItemId}", cartItemId)
// 				.param("quantity", String.valueOf(quantity)))
// 			.andExpect(status().isOk());
// 	}
//
// 	@Test
// 	@DisplayName("장바구니 아이템 삭제 성공")
// 	void removeCartItem_Success() throws Exception {
// 		UUID cartItemId = UUID.randomUUID();
//
// 		doNothing().when(cartService).deleteItem(cartItemId);
//
// 		mockMvc.perform(delete("/api/cart/items/{cartItemId}", cartItemId))
// 			.andExpect(status().isOk());
// 	}
// }
