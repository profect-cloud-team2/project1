package com.example.demo.menus.controller;

import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.dto.MenuUpdateRequestDto;
import com.example.demo.menus.service.MenuService;
import com.example.demo.global.jwt.customJwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto) {
        MenuResponseDto response = menuService.createMenu(requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable UUID menuId,
            @RequestBody MenuUpdateRequestDto requestDto) {
        return ResponseEntity.ok(menuService.updateMenu(menuId, requestDto));
    }

    @PatchMapping("/{menuId}/delete")
    public ResponseEntity<Void> softDeleteMenu(@PathVariable UUID menuId, Authentication authentication) {
        // Authentication에서 userId 추출
        UUID userId = UUID.fromString(authentication.getName());
        menuService.softDeleteMenu(menuId, userId);
        return ResponseEntity.noContent().build();
    }
}
