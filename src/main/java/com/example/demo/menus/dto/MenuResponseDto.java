package com.example.demo.menus.dto;

import com.example.demo.menus.entity.MenuStatus;
import com.example.demo.store.entity.StoreEntity;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDto {
    private UUID menuId;
    private UUID storeId;
    private String name;
    private String img;
    private Integer price;
    private String introduction;
    private Integer requiredTime;
    private MenuStatus isAvailable;
}

