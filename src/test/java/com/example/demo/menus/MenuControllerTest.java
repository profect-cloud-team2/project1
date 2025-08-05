package com.example.demo.menus;

import com.example.demo.menus.controller.MenuController;
import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.dto.MenuUpdateRequestDto;
import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.menus.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static com.example.demo.menus.entity.MenuStatus.ONSALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    // createMenu 테스트
    @Test
    @DisplayName("createMenu - 성공 시 200 OK와 응답 반환")
    void createMenu_success() {
        MenuResponseDto responseDto = new MenuResponseDto(
                UUID.randomUUID(), UUID.randomUUID(), "Menu1",
                "https://example.com/images/test.jpg", 1000, "Intro",
                10, MenuStatus.ONSALE
        );

        when(menuService.createMenu(any(MenuRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<MenuResponseDto> response = menuController.createMenu(
                new MenuRequestDto(
                        UUID.randomUUID(), "Menu1","https://example.com/images/test.jpg",
                        1000, "Intro", 10, MenuStatus.ONSALE)
        );

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("createMenu - IllegalArgumentException 발생 시 예외 전파")
    void createMenu_illegalArgument() {
        when(menuService.createMenu(any(MenuRequestDto.class)))
                .thenThrow(new IllegalArgumentException("잘못된 요청"));

        assertThatThrownBy(() -> menuController.createMenu(
                new MenuRequestDto(UUID.randomUUID(), "Menu1","https://example.com/images/test.jpg",
                        1000, "Intro", 10, MenuStatus.ONSALE)
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 요청");
    }

    // updateMenu 테스트
    @Test
    @DisplayName("updateMenu - 성공 시 200 OK와 응답 반환")
    void updateMenu_success() {
        UUID menuId = UUID.randomUUID();
        MenuResponseDto responseDto = new MenuResponseDto(
                UUID.randomUUID(),UUID.randomUUID(),"Updated Menu","https://example.com/images/updatetest.jpg",
                2000, "Updated Intro",10, MenuStatus.SOLDOUT
        );

        when(menuService.updateMenu(eq(menuId), any(MenuUpdateRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<MenuResponseDto> response = menuController.updateMenu(
                menuId,
                new MenuUpdateRequestDto(UUID.randomUUID(),"Updated Menu","https://example.com/images/updatedtest.jpg",
                        2000,"Updated Intro",MenuStatus.SOLDOUT)
        );

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("updateMenu - IllegalStateException 발생 시 예외 전파")
    void updateMenu_illegalState() {
        UUID menuId = UUID.randomUUID();
        when(menuService.updateMenu(eq(menuId), any(MenuUpdateRequestDto.class)))
                .thenThrow(new IllegalStateException("수정 불가 상태"));

        assertThatThrownBy(() -> menuController.updateMenu(
                menuId,
                new MenuUpdateRequestDto(UUID.randomUUID(),"Updated Menu","https://example.com/images/updatedtest.jpg",
                        2000,"Updated Intro",MenuStatus.SOLDOUT)
        )).isInstanceOf(IllegalStateException.class)
                .hasMessage("수정 불가 상태");
    }

    // softDeleteMenu 테스트
    @Test
    @DisplayName("softDeleteMenu - 성공 시 204 No Content 반환")
    void softDeleteMenu_success() {
        UUID menuId = UUID.randomUUID();

        // Authentication Mock
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(UUID.randomUUID().toString());

        ResponseEntity<Void> response = menuController.softDeleteMenu(menuId, authentication);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    @DisplayName("softDeleteMenu - IllegalArgumentException 발생 시 예외 전파")
    void softDeleteMenu_illegalArgument() {
        UUID menuId = UUID.randomUUID();

        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(UUID.randomUUID().toString());

        // Service에서 예외 발생하도록 Mock
        org.mockito.Mockito.doThrow(new IllegalArgumentException("삭제 실패"))
                .when(menuService).softDeleteMenu(eq(menuId), any(UUID.class));

        assertThatThrownBy(() -> menuController.softDeleteMenu(menuId, authentication))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제 실패");
    }
}
