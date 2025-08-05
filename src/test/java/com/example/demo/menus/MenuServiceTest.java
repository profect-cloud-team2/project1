package com.example.demo.menus;

import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.dto.MenuUpdateRequestDto;
import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.menus.service.MenuCacheService;
import com.example.demo.menus.service.MenuService;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.entity.StoreStatus;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuCacheService menuCacheService;

    @InjectMocks
    private MenuService menuService;

    private StoreEntity store;
    private MenuEntity menu;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = UserEntity.builder()
                .userId(UUID.randomUUID())
                .name("Tester")
                .birthdate(LocalDate.of(2000, 1, 1))
                .phone("01012345678")
                .nickname("tester")
                .email("test@example.com")
                .password("password")
                .build();

        store = StoreEntity.builder()
                .storeId(UUID.randomUUID())
                .user(user)
                .name("Test Store")
                .businessNum("1234567890")
                .category(Category.KOREAN)
                .address1("Seoul")
                .address2("Gangnam")
                .phoneNum("01012345678")
                .openTime(LocalTime.of(9, 0))
                .closedTime(LocalTime.of(21, 0))
                .isAvailable(StoreStatus.OPEN)
                .createdBy(user.getUserId())
                .build();

        menu = MenuEntity.builder()
                .menuId(UUID.randomUUID())
                .store(store)
                .name("Menu1")
                .price(1000)
                .introduction("Intro")
                .requiredTime(10)
                .isAvailable(MenuStatus.ONSALE)
                .build();
    }

    @Test
    @DisplayName("createMenu - 성공 시 메뉴 생성 후 반환")
    void createMenu_success() {

        UUID menuId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        // given
        MenuRequestDto requestDto = new MenuRequestDto(
                UUID.randomUUID(),"Menu1","https://example.com/images/test.jpg",
                1000, "Intro", 10, MenuStatus.ONSALE
        );
        when(storeRepository.findByStoreIdAndDeletedAtIsNull(any(UUID.class))).thenReturn(Optional.of(store));
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menu);

        MenuResponseDto result = menuService.createMenu(requestDto);

        assertThat(result.getName()).isEqualTo("Menu1");
        assertThat(result.getPrice()).isEqualTo(1000);
        verify(menuRepository, times(1)).save(any(MenuEntity.class));

        System.out.println("🧪 메뉴 등록 테스트 결과:");
        System.out.println(" - 등록 이름: " + result.getName());
        System.out.println(" - 가격: " + result.getPrice());
        System.out.println(" - 소개글: " + result.getIntroduction());
    }

    @Test
    @DisplayName("createMenu - 매장 없음 예외 발생")
    void createMenu_storeNotFound() {
        // given
        MenuRequestDto requestDto = new MenuRequestDto(
                UUID.randomUUID(),"Menu1","https://example.com/images/test.jpg",
                1000, "Intro", 10, MenuStatus.ONSALE
        );

        when(storeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(storeRepository.findById(any(UUID.class))).thenReturn(Optional.of(store));


        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(requestDto));

        verify(menuRepository, never()).save(any(MenuEntity.class));
    }

    @Test
    @DisplayName("updateMenu - 성공 시 메뉴 수정 후 반환")
    void updateMenu_success() {

        UUID menuId = menu.getMenuId();

        MenuUpdateRequestDto updateDto = new MenuUpdateRequestDto(
                store.getStoreId(),
                "Updated Menu",
                "https://example.com/images/updatedtest.jpg",
                2000,
                "Updated Intro",
                MenuStatus.SOLDOUT
        );

        when(menuRepository.findByMenuIdAndDeletedAtIsNull(eq(menuId))).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menu);

        // when
        MenuResponseDto result = menuService.updateMenu(menuId, updateDto);

        // then
        assertThat(result.getName()).isEqualTo("Updated Menu"); // 실제 엔티티 수정 로직에 맞게 바꿔야 함
        verify(menuRepository, times(1)).save(any(MenuEntity.class));

        doNothing().when(menuCacheService).evictMenus(anyString());

        System.out.println("🧪 메뉴 수정 테스트 결과:");
        System.out.println(" - 수정 이름: " + result.getName());
        System.out.println(" - 가격: " + result.getPrice());
        System.out.println(" - 소개글: " + result.getIntroduction());
    }

    @Test
    @DisplayName("updateMenu - 메뉴 없음 예외 발생")
    void updateMenu_notFound() {
        // given
        UUID menuId = UUID.randomUUID();

        when(menuRepository.findByMenuIdAndDeletedAtIsNull(eq(menuId))).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> menuService.updateMenu(menuId, new MenuUpdateRequestDto()));

        verify(menuRepository, never()).save(any(MenuEntity.class));
    }

    @Test
    @DisplayName("softDeleteMenu - 성공 시 deletedAt 설정")
    void softDeleteMenu_success() {
        // given
        UUID menuId = menu.getMenuId();
        UUID userId = menu.getStore().getUser().getUserId();

        when(menuRepository.findByMenuIdAndDeletedAtIsNull(eq(menuId))).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menu);

        // when
        menuService.softDeleteMenu(menuId,userId);

        // then
        assertThat(menu.getDeletedAt()).isNotNull();
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    @DisplayName("softDeleteMenu - 메뉴 없음 예외 발생")
    void softDeleteMenu_notFound() {
        // given
        UUID menuId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(menuRepository.findByMenuIdAndDeletedAtIsNull(eq(menuId))).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> menuService.softDeleteMenu(menuId, userId));

        verify(menuRepository, never()).save(any(MenuEntity.class));
    }
}
