package com.example.demo.menus.service;

import com.example.demo.ai.entity.AiEntity;
import com.example.demo.ai.repository.AiRepository;
import com.example.demo.menus.dto.MenuAiResponseDto;
import com.example.demo.menus.dto.MenuRequestDto;
import com.example.demo.menus.dto.MenuResponseDto;
import com.example.demo.menus.dto.MenuUpdateRequestDto;
import com.example.demo.menus.service.ai.MenuOpenAiClient;
import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static io.lettuce.core.ShutdownArgs.Builder.save;
import static java.awt.SystemColor.menu;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuCacheService menuCacheService;
    private final MenuAiService menuAiService;
    private final AiRepository aiRepository;

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {

        StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        if (menuRepository.existsByStoreAndNameAndDeletedAtIsNull(store, requestDto.getName())) {
            throw new IllegalArgumentException("이미 등록된 메뉴입니다.");
        }

        MenuAiResponseDto menuAiResponseDto =
                menuAiService.generateMenuIntroduction(requestDto.getName(), requestDto.getIntroduction());

        AiEntity log = AiEntity.builder()
                .apiType("MENU_DESC")
                .requestJson(menuAiResponseDto.getRequest())
                .responseJson(menuAiResponseDto.getResponse())
                .createdBy(store.getCreatedBy())  
                .build();
        aiRepository.save(log);

        MenuEntity menu = MenuEntity.builder()
                .store(store)
                .name(requestDto.getName())
                .imgURL(requestDto.getImg())
                .price(requestDto.getPrice())
                .introduction(requestDto.getIntroduction())
                .aiDescription(menuAiResponseDto.getResponse()) 
                .requiredTime(requestDto.getRequiredTime())
                .isAvailable(MenuStatus.ONSALE)
                .build();

        MenuEntity savedMenu = menuRepository.save(menu);

        menuCacheService.evictMenus(store.getStoreId().toString());

        return MenuResponseDto.builder()
                .menuId(savedMenu.getMenuId())
                .storeId(savedMenu.getStore().getStoreId())
                .name(savedMenu.getName())
                .img(savedMenu.getImgURL())
                .price(savedMenu.getPrice())
                .introduction(savedMenu.getIntroduction())
                .aiDescription(savedMenu.getAiDescription())
                .requiredTime(savedMenu.getRequiredTime())
                .isAvailable(savedMenu.getIsAvailable())
                .build();
    }


    public MenuResponseDto updateMenu(UUID menuId, MenuUpdateRequestDto requestDto) {
      
        MenuEntity menu = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        if (requestDto.getName() != null) menu.setName(requestDto.getName());
        if (requestDto.getImgURL() != null) menu.setImgURL(requestDto.getImgURL());
        if (requestDto.getPrice() != null) menu.setPrice(requestDto.getPrice());
        if (requestDto.getIntroduction() != null) menu.setIntroduction(requestDto.getIntroduction());
        if (requestDto.getIsAvailable() != null) {
            menu.setIsAvailable(MenuStatus.ONSALE);
        }

        MenuAiResponseDto menuAiResponseDto =
                menuAiService.generateMenuIntroduction(requestDto.getName(), requestDto.getIntroduction());
      
        AiEntity log = AiEntity.builder()
                .apiType("MENU_DESC")
                .requestJson(menuAiResponseDto.getRequest())
                .responseJson(menuAiResponseDto.getResponse())
                .createdBy(menu.getCreatedBy())
                .build();
        aiRepository.save(log);

        menuCacheService.evictMenus(menu.getStore().getStoreId().toString());

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

    public void softDeleteMenu(UUID menuId, UUID userId) {
        MenuEntity menu = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        menu.setDeletedAt(LocalDateTime.now());
        menu.setDeletedBy(userId);

        menuCacheService.evictMenus(menu.getStore().getStoreId().toString());

        menuRepository.save(menu);
    }

    public List<MenuResponseDto> getMenusByStore(UUID storeId) {

        List<MenuResponseDto> cachedMenus = menuCacheService.getCachedMenus(storeId.toString());
        if (cachedMenus != null) {
            return cachedMenus;
        }

        StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(storeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        List<MenuEntity> menus = menuRepository.findByStoreAndDeletedAtIsNull(store);


        List<MenuResponseDto> result = menus.stream()
            .map(menu -> MenuResponseDto.builder()
                .menuId(menu.getMenuId())
                .storeId(menu.getStore().getStoreId())
                .name(menu.getName())
                .img(menu.getImgURL())
                .price(menu.getPrice())
                .introduction(menu.getIntroduction())
                .requiredTime(menu.getRequiredTime())
                .isAvailable(menu.getIsAvailable())
                .build())
            .toList();

        menuCacheService.cacheMenus(storeId.toString(), result);

        return result;
    }
}
