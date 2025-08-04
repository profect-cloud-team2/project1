package com.example.demo.menus.controller;

import com.example.demo.menus.dto.MenuIntroductionRequestDto;
import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.dto.MenuUpdateRequestDto;
import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.menus.service.MenuService;
import com.example.demo.global.jwt.customJwtFilter;
import com.example.demo.menus.service.ai.MenuOpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final MenuOpenAiClient menuOpenAiClient;

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto) {
        MenuResponseDto response = menuService.createMenu(requestDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @PatchMapping("{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
        @PathVariable UUID menuId,
        @RequestBody MenuUpdateRequestDto requestDto) {
        return ResponseEntity.ok(menuService.updateMenu(menuId, requestDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @PatchMapping("/{menuId}/delete")
    public ResponseEntity<Void> softDeleteMenu(@PathVariable UUID menuId, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        menuService.softDeleteMenu(menuId, userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('CUSTOMER')")
    @PostMapping("/{menuId}/introduction")
    public ResponseEntity<String> generateMenuIntroduction(@PathVariable UUID menuId) {
        MenuEntity menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

        MenuIntroductionRequestDto request = new MenuIntroductionRequestDto(
            menu.getName(),
            menu.getIntroduction()
        );

        String introduction = menuOpenAiClient.generateIntroduction(request);
        return ResponseEntity.ok(introduction);
    }

    // ✅ [GET] storeId 기준으로 메뉴 목록 조회 (캐시 적용)
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('CUSTOMER')")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<MenuResponseDto>> getMenusByStore(@PathVariable UUID storeId) {
        List<MenuResponseDto> menus = menuService.getMenusByStore(storeId);
        return ResponseEntity.ok(menus);
    }
}

