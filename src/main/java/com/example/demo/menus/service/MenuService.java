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

    // 메뉴 생성
    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {
        // 1. StoreEntity 조회
        StoreEntity store = storeRepository.findByStoreIdAndDeletedAtIsNull(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        // 2. 중복 체크
        if (menuRepository.existsByStoreAndNameAndDeletedAtIsNull(store, requestDto.getName())) {
            throw new IllegalArgumentException("이미 등록된 메뉴입니다.");
        }

        // 3. AI 메뉴 소개 문구 생성 (이름/카테고리 기반)
        MenuAiResponseDto menuAiResponseDto =
                menuAiService.generateMenuIntroduction(requestDto.getName(), requestDto.getIntroduction());

        // 4. AI 호출 로그 저장
        AiEntity log = AiEntity.builder()
                .apiType("MENU_DESC")
                .requestJson(menuAiResponseDto.getRequest())
                .responseJson(menuAiResponseDto.getResponse())
                .createdBy(store.getCreatedBy())  // 또는 로그인 사용자 ID
                .build();
        aiRepository.save(log);

        // 5. MenuEntity 생성
        MenuEntity menu = MenuEntity.builder()
                .store(store)
                .name(requestDto.getName())
                .imgURL(requestDto.getImg())
                .price(requestDto.getPrice())
                .introduction(requestDto.getIntroduction())
                .aiDescription(menuAiResponseDto.getResponse())  // AI 결과 저장
                .requiredTime(requestDto.getRequiredTime())
                .isAvailable(MenuStatus.ONSALE)
                .build();

        // 6. 저장
        MenuEntity savedMenu = menuRepository.save(menu);

        // 7. 캐시 무효화
        menuCacheService.evictMenus(store.getStoreId().toString());

        // 8. 응답 반환
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


    // 메뉴 수정
    public MenuResponseDto updateMenu(UUID menuId, MenuUpdateRequestDto requestDto) {
        // 1. 메뉴 조회
        MenuEntity menu = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        // 2. 수정
        if (requestDto.getName() != null) menu.setName(requestDto.getName());
        if (requestDto.getImgURL() != null) menu.setImgURL(requestDto.getImgURL());
        if (requestDto.getPrice() != null) menu.setPrice(requestDto.getPrice());
        if (requestDto.getIntroduction() != null) menu.setIntroduction(requestDto.getIntroduction());
        if (requestDto.getIsAvailable() != null) {
            menu.setIsAvailable(MenuStatus.ONSALE);
        }

        // 3. AI 메뉴 소개 문구 생성 (이름/카테고리 기반)
        MenuAiResponseDto menuAiResponseDto =
                menuAiService.generateMenuIntroduction(requestDto.getName(), requestDto.getIntroduction());

        // 4. AI 호출 로그 저장
        AiEntity log = AiEntity.builder()
                .apiType("MENU_DESC")
                .requestJson(menuAiResponseDto.getRequest())
                .responseJson(menuAiResponseDto.getResponse())
                .createdBy(menu.getCreatedBy())  // 또는 로그인 사용자 ID
                .build();
        aiRepository.save(log);

        // 3. 캐시 무효화
        menuCacheService.evictMenus(menu.getStore().getStoreId().toString());

        // 4. 응답 반환
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

//    public MenuAiResponseDto generateMenuIntroduction(UUID menuId) {
//        MenuEntity menu = menuRepository.findById(menuId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));
//
//        String prompt = String.format(
//                "메뉴 이름: %s, 현재 소개: %s\n" +
//                        "50자 이내로 고객의 시선을 끌 수 있는 참신하고 매력적인 소개 문구를 작성해 주세요.",
//                menu.getName(),
//                menu.getIntroduction() == null ? "" : menu.getIntroduction()
//        );
//
//        return new MenuAiResponseDto(prompt, content);
//
//    }

    // 메뉴 목록 조회 (Redis 캐시 적용)
    public List<MenuResponseDto> getMenusByStore(UUID storeId) {
        // 1. Redis에서 캐시 조회
        List<MenuResponseDto> cachedMenus = menuCacheService.getCachedMenus(storeId.toString());
        if (cachedMenus != null) {
            return cachedMenus;
        }

        // 2. DB에서 조회
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

        // 3. Redis에 저장
        menuCacheService.cacheMenus(storeId.toString(), result);

        return result;
    }
}
