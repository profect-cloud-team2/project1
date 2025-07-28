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
        Menu menu = Menu.builder()
                .store_id(requestDto.getStore_id())
                .name(requestDto.getName())
                .img(requestDto.getImg())
                .price(requestDto.getPrice())
                .build();

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.builder()
                .id(savedMenu.getId())
                .store_id(savedMenu.getStore_id())
                .name(savedMenu.getName())
                .img(savedMenu.getImg())
                .price(savedMenu.getPrice())
                .build();
    }
}
