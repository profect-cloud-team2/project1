package com.example.demo.menus.dto;

import com.example.demo.store.entity.StoreEntity;
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
    private String name;
    private String img;
    private Integer price;
    private String introduction;
    private Integer requiredTime;
    private String isAvailable; //enum
}
