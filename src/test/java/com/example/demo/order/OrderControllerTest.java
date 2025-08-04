package com.example.demo.order;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.order.controller.OrderController;
import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Mock
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		OrderController orderController = new OrderController(orderService);
		mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("주문 생성 성공")
	void createOrder_Success() throws Exception {
		OrderCreateReq request = new OrderCreateReq();
		request.setStoreId(UUID.randomUUID());
		request.setCartItemIds(Arrays.asList(UUID.randomUUID()));
		request.setRequestMessage("요청사항");

		doNothing().when(orderService).createOrder(any(OrderCreateReq.class), any(UUID.class));

		mockMvc.perform(post("/api/order/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.param("userIdStr", UUID.randomUUID().toString()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("주문 취소 성공")
	void cancelOrder_Success() throws Exception {
		UUID orderId = UUID.randomUUID();

		doNothing().when(orderService).cancelOrder(eq(orderId), any(UUID.class));

		mockMvc.perform(patch("/api/order/{orderId}/cancel", orderId)
				.param("userIdStr", UUID.randomUUID().toString()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("주문 상태 업데이트 성공")
	void updateOrderStatus_Success() throws Exception {
		UUID orderId = UUID.randomUUID();
		String requestJson = "{\"orderStatus\":\"배달중\"}";

		doNothing().when(orderService).updateOrderStatus(eq(orderId), eq(OrderStatus.배달중), any(UUID.class));

		mockMvc.perform(patch("/api/order/{orderId}/status", orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)
				.param("userIdStr", UUID.randomUUID().toString()))
			.andExpect(status().isOk());
	}
}
