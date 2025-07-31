package com.example.demo.menus.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
public class MenuUpdateRequestDto {
    private UUID menuId;
    private String name;
    private String imgURL;          // 이미지 URL
    private Integer price;
    private String introduction;
    private String isAvailable;  // 판매중 / 품절 상태
}
