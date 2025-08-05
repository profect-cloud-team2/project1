package com.example.demo.menus.dto;

import com.example.demo.menus.entity.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateRequestDto {
    private UUID menuId;
    private String name;
    private String imgURL;          // 이미지 URL
    private Integer price;
    private String introduction;
    private MenuStatus isAvailable;  // 판매중 / 품절 상태
}
