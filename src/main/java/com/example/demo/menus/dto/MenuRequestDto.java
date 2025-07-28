package com.example.demo.menus.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {
    private Long storeId;
    private String name;
    private String img;
    private Integer price;
}
