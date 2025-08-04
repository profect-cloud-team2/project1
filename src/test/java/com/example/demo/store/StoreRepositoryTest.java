package com.example.demo.store;

import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreRepositoryTest {

    @Mock
    private StoreRepository storeRepository;

    @Test
    @DisplayName("StoreId로 삭제되지 않은 매장 조회 (Mock)")
    void findByStoreIdAndDeletedAtIsNull() {
        // given
        StoreEntity store = createStore();
        UUID storeId = store.getStoreId();

        // when
        when(storeRepository.findByStoreIdAndDeletedAtIsNull(storeId))
                .thenReturn(Optional.of(store));

        // then
        Optional<StoreEntity> found = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Store");
    }

    @Test
    @DisplayName("사업자 번호로 삭제되지 않은 매장 조회 (Mock)")
    void findByBusinessNumAndDeletedAtIsNull() {

        StoreEntity store = createStore();
        String businessNum = store.getBusinessNum();

        when(storeRepository.findByBusinessNumAndDeletedAtIsNull(businessNum))
                .thenReturn(Optional.of(store));

        Optional<StoreEntity> found = storeRepository.findByBusinessNumAndDeletedAtIsNull(businessNum);
        assertThat(found).isPresent();
        assertThat(found.get().getBusinessNum()).isEqualTo("1234567890");
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .userId(UUID.randomUUID())
                .name("Tester")
                .birthdate(LocalDate.of(2000, 1, 1))
                .phone("01012345678")
                .nickname("tester")
                .email("test@example.com")
                .password("password")
                .build();
    }

    private StoreEntity createStore() {
        return StoreEntity.builder()
                .storeId(UUID.randomUUID())
                .user(createUser())
                .name("Test Store")
                .businessNum("1234567890")
                .category(Category.KOREAN)
                .address1("Seoul")
                .address2("Gangnam")
                .phoneNum("01012345678")
                .openTime(LocalTime.of(9, 0))
                .closedTime(LocalTime.of(21, 0))
                .isAvailable(StoreStatus.OPEN)
                .createdBy(UUID.randomUUID())
                .build();
    }
}
