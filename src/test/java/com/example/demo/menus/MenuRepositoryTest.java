package com.example.demo.menus;

import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
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
class MenuRepositoryTest {

    @Mock
    private MenuRepository menuRepository;

    @Test
    @DisplayName("Store와 name으로 메뉴 존재 여부 확인 (Mock)")
    void existsByStoreAndNameAndDeletedAtIsNull() {

        StoreEntity store = createStore();
        String menuName = "Menu1";

        when(menuRepository.existsByStoreAndNameAndDeletedAtIsNull(store, menuName))
                .thenReturn(true);

        boolean exists = menuRepository.existsByStoreAndNameAndDeletedAtIsNull(store, menuName);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("MenuId로 삭제되지 않은 메뉴 찾기 (Mock)")
    void findByMenuIdAndDeletedAtIsNull() {
        // given
        UUID menuId = UUID.randomUUID();
        MenuEntity menu = createMenu(createStore(), "Menu1");

        // when
        when(menuRepository.findByMenuIdAndDeletedAtIsNull(menuId))
                .thenReturn(Optional.of(menu));

        // then
        Optional<MenuEntity> found = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Menu1");
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

    private MenuEntity createMenu(StoreEntity store, String name) {
        return MenuEntity.builder()
                .menuId(UUID.randomUUID())
                .store(store)
                .name(name)
                .price(1000)
                .introduction("Intro")
                .requiredTime(10)
                .isAvailable(MenuStatus.ONSALE)
                .build();
    }
}

