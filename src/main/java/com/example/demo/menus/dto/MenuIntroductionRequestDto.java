package com.example.demo.menus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuIntroductionRequestDto {
    private String name;     // 메뉴 이름
    private String introduction;  // 메뉴 설명
}
