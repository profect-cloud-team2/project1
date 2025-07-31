package com.example.demo.menus.dto;

import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.store.entity.StoreEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {
    private UUID storeId;

    @NotNull(message = "메뉴명은 필수입니다.")
    private String name;

    private String img;

    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    private String introduction;

    @Schema(description = "조리 시간(분 단위)", example = "10")
    @NotNull(message = "조리 시간은 필수입니다.")
    private Integer requiredTime;

    private MenuStatus isAvailable; //enum
}
