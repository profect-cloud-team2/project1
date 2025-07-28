package com.example.demo.menus.service;

import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.entity.Menu;
import com.example.demo.menus.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuResponseDto createMenu(MenuRequestDto requestDto) {
        if (menuRepository.existsByStoreIdAndName(requestDto.getStoreId(), requestDto.getName())) {
            throw new IllegalArgumentException("이미 등록된 메뉴입니다.");
        }

        Menu menu = Menu.builder()
                .storeId(requestDto.getStoreId())
                .name(requestDto.getName())
                .img(requestDto.getImg())
                .price(requestDto.getPrice())
                .build();

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.builder()
                .id(savedMenu.getId())
                .storeId(savedMenu.getStoreId())
                .name(savedMenu.getName())
                .img(savedMenu.getImg())
                .price(savedMenu.getPrice())
                .build();
    }
}
