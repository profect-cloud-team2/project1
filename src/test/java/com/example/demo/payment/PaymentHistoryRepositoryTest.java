package com.example.demo.payment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
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

import com.example.demo.payment.entity.PaymentHistoryEntity;
import com.example.demo.payment.repository.PaymentHistoryRepository;

@ExtendWith(MockitoExtension.class)
class PaymentHistoryRepositoryTest {

	@Mock
	private PaymentHistoryRepository paymentHistoryRepository;

	@Test
	@DisplayName("사용자별 결제내역 조회")
	void findByUserIdAndDeletedAtIsNull() {
		UUID userId = UUID.randomUUID();
		Pageable pageable = PageRequest.of(0, 10);
		PaymentHistoryEntity payment = new PaymentHistoryEntity();
		payment.setPaymentHistoryId(UUID.randomUUID());
		Page<PaymentHistoryEntity> paymentPage = new PageImpl<>(Arrays.asList(payment));

		given(paymentHistoryRepository.findByUserIdAndDeletedAtIsNull(userId, pageable)).willReturn(paymentPage);

		Page<PaymentHistoryEntity> result = paymentHistoryRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);

		assertThat(result.getContent()).hasSize(1);
	}

	@Test
	@DisplayName("주문ID로 결제내역 조회")
	void findByOrderOrderIdAndDeletedAtIsNull() {
		UUID orderId = UUID.randomUUID();
		PaymentHistoryEntity payment = new PaymentHistoryEntity();
		payment.setPaymentHistoryId(UUID.randomUUID());

		given(paymentHistoryRepository.findByOrderOrderIdAndDeletedAtIsNull(orderId)).willReturn(Optional.of(payment));

		Optional<PaymentHistoryEntity> result = paymentHistoryRepository.findByOrderOrderIdAndDeletedAtIsNull(orderId);

		assertThat(result).isPresent();
	}
}
