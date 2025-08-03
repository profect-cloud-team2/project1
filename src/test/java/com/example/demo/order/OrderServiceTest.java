package com.example.demo.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.order.dto.OrderCreateReq;
import com.example.demo.order.dto.OrderDetailResponseDto;
import com.example.demo.order.dto.OrderMenuResponseDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.service.OrderService;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderService orderService;

	@Test
	@DisplayName("주문 생성 성공")
	void createOrder_Success() {
		OrderCreateReq request = new OrderCreateReq();
		request.setStoreId(UUID.randomUUID());
		request.setCartItemIds(Arrays.asList(UUID.randomUUID()));
		request.setRequestMessage("요청사항");
		UUID userId = UUID.randomUUID();

		doNothing().when(orderService).createOrder(any(OrderCreateReq.class), any(UUID.class));

		orderService.createOrder(request, userId);
	}

	@Test
	@DisplayName("주문 취소 성공")
	void cancelOrder_Success() {
		UUID orderId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();

		doNothing().when(orderService).cancelOrder(orderId, userId);

		orderService.cancelOrder(orderId, userId);
	}

	@Test
	@DisplayName("사용자 주문 조회 성공")
	void getUserOrders_Success() {
		UUID userId = UUID.randomUUID();
		Pageable pageable = PageRequest.of(0, 10);
		OrderResponseDto orderDto = OrderResponseDto.builder()
			.orderId(UUID.randomUUID())
			.storeName("테스트가게")
			.totalPrice(10000)
			.build();
		Page<OrderResponseDto> orderPage = new PageImpl<>(Arrays.asList(orderDto));

		given(orderService.getUserOrders(userId, pageable)).willReturn(orderPage);

		Page<OrderResponseDto> result = orderService.getUserOrders(userId, pageable);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getStoreName()).isEqualTo("테스트가게");
	}

	@Test
	@DisplayName("주문 메뉴 조회 성공")
	void getMyOrdersWithMenu_Success() {
		UUID userId = UUID.randomUUID();
		Pageable pageable = PageRequest.of(0, 10);
		OrderMenuResponseDto.MenuItemDto menuItem = OrderMenuResponseDto.MenuItemDto.builder()
			.menuName("아메리카노")
			.quantity(2)
			.price(3000)
			.build();
		OrderMenuResponseDto orderMenuDto = OrderMenuResponseDto.builder()
			.orderId(UUID.randomUUID())
			.menuItems(Arrays.asList(menuItem))
			.build();
		Page<OrderMenuResponseDto> orderPage = new PageImpl<>(Arrays.asList(orderMenuDto));

		given(orderService.getMyOrdersWithMenu(userId, pageable)).willReturn(orderPage);

		Page<OrderMenuResponseDto> result = orderService.getMyOrdersWithMenu(userId, pageable);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getMenuItems()).hasSize(1);
	}

	@Test
	@DisplayName("주문 상세 조회 성공")
	void getOrderDetail_Success() {
		UUID orderId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		OrderDetailResponseDto orderDetail = OrderDetailResponseDto.builder()
			.orderId(orderId)
			.storeName("테스트가게")
			.totalPrice(10000)
			.build();

		given(orderService.getOrderDetail(orderId, userId)).willReturn(orderDetail);

		OrderDetailResponseDto result = orderService.getOrderDetail(orderId, userId);

		assertThat(result.getOrderId()).isEqualTo(orderId);
		assertThat(result.getStoreName()).isEqualTo("테스트가게");
	}

	@Test
	@DisplayName("주문 상태 업데이트 성공")
	void updateOrderStatus_Success() {
		UUID orderId = UUID.randomUUID();
		OrderStatus newStatus = OrderStatus.배달중;
		UUID ownerId = UUID.randomUUID();

		doNothing().when(orderService).updateOrderStatus(orderId, newStatus, ownerId);

		orderService.updateOrderStatus(orderId, newStatus, ownerId);
	}
}
