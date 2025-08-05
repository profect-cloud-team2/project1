package com.example.demo.order;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

	@Mock
	private OrderRepository orderRepository;

	@Test
	@DisplayName("가게별 주문 조회")
	void findByStoreIdAndDeletedAtIsNull() {
		UUID storeId = UUID.randomUUID();
		Pageable pageable = PageRequest.of(0, 10);
		OrderEntity order = new OrderEntity();
		order.setOrderId(UUID.randomUUID());
		Page<OrderEntity> orderPage = new PageImpl<>(Arrays.asList(order));

		given(orderRepository.findByStoreIdAndDeletedAtIsNull(storeId, pageable)).willReturn(orderPage);

		Page<OrderEntity> result = orderRepository.findByStoreIdAndDeletedAtIsNull(storeId, pageable);

		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	@DisplayName("사용자별 주문 조회")
	void findByUserUserIdAndDeletedAtIsNull() {
		UUID userId = UUID.randomUUID();
		Pageable pageable = PageRequest.of(0, 10);
		OrderEntity order = new OrderEntity();
		order.setOrderId(UUID.randomUUID());
		Page<OrderEntity> orderPage = new PageImpl<>(Arrays.asList(order));

		given(orderRepository.findByUserUserIdAndDeletedAtIsNull(userId, pageable)).willReturn(orderPage);

		Page<OrderEntity> result = orderRepository.findByUserUserIdAndDeletedAtIsNull(userId, pageable);

		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	@DisplayName("주문 ID로 조회")
	void findByIdAndDeletedAtIsNull() {
		UUID orderId = UUID.randomUUID();
		OrderEntity order = new OrderEntity();
		order.setOrderId(orderId);

		given(orderRepository.findByIdAndDeletedAtIsNull(orderId)).willReturn(Optional.of(order));

		Optional<OrderEntity> result = orderRepository.findByIdAndDeletedAtIsNull(orderId);

		assertThat(result).isPresent();
	}

	@Test
	@DisplayName("10분 초과 주문 조회")
	void findByOrderStatusAndCreatedAtBeforeAndDeletedAtIsNull() {
		OrderStatus status = OrderStatus.RECEIVED;
		LocalDateTime time = LocalDateTime.now().minusMinutes(10);
		OrderEntity order = new OrderEntity();
		order.setOrderId(UUID.randomUUID());

		given(orderRepository.findByOrderStatusAndCreatedAtBeforeAndDeletedAtIsNull(status, time))
			.willReturn(Arrays.asList(order));

		List<OrderEntity> result = orderRepository.findByOrderStatusAndCreatedAtBeforeAndDeletedAtIsNull(status, time);

		assertThat(result).hasSize(1);
	}
}
