package com.example.demo.menus.service;

import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.dto.MenuUpdateRequestDto;
import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository; // Store 조회를 위해 필요

    public MenuResponseDto createMenu(MenuRequestDto requestDto) {

        // 1. StoreEntity 조회 (존재하지 않으면 예외 발생)
        StoreEntity store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        // 2. 중복 체크 (store와 name으로 중복 여부 확인)
        if (menuRepository.existsByStoreAndName(store, requestDto.getName())) {
            throw new IllegalArgumentException("이미 등록된 메뉴입니다.");
}

        // 3. MenuEntity 생성
        MenuEntity menu = MenuEntity.builder()
                .store(store)  // 객체 연관관계로 설정
                .name(requestDto.getName())
                .imgURL(requestDto.getImg())   // 엔티티 필드명에 맞게 수정
                .price(requestDto.getPrice())
                .introduction(requestDto.getIntroduction())
                .requiredTime(requestDto.getRequiredTime())
                .isAvailable(MenuStatus.ONSALE) // 기본 상태
                .build();

        // 4. 저장
        MenuEntity savedMenu = menuRepository.save(menu);

        // 5. Response 변환
        return MenuResponseDto.builder()
                .menuId(savedMenu.getMenuId())
                .storeId(savedMenu.getStore().getStoreId())
                .name(savedMenu.getName())
                .img(savedMenu.getImgURL())
                .price(savedMenu.getPrice())
                .introduction(savedMenu.getIntroduction())
                .requiredTime(savedMenu.getRequiredTime())
                .isAvailable(savedMenu.getIsAvailable())
                .build();

    }


    public MenuResponseDto updateMenu(UUID menuId, MenuUpdateRequestDto requestDto) {
        // 1. 메뉴 조회
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        // 2. 값 업데이트 (null 값 무시)
        if (requestDto.getName() != null) menu.setName(requestDto.getName());
        if (requestDto.getImgURL() != null) menu.setImgURL(requestDto.getImgURL());
        if (requestDto.getPrice() != null) menu.setPrice(requestDto.getPrice());
        if (requestDto.getIntroduction() != null) menu.setIntroduction(requestDto.getIntroduction());
        if (requestDto.getIsAvailable() != null) {
            menu.setIsAvailable(MenuStatus.ONSALE);
        }
        return MenuResponseDto.builder()
                .menuId(menu.getMenuId())
                .storeId(menu.getStore().getStoreId())
                .name(menu.getName())
                .img(menu.getImgURL())
                .price(menu.getPrice())
                .introduction(menu.getIntroduction())
                .requiredTime(menu.getRequiredTime())
                .isAvailable(menu.getIsAvailable())
                .build();
    }
}
