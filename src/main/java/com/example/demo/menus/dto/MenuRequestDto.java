package com.example.demo.menus.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {
    private Long store_id;
    private String name;
    private String img;
    private Integer price;
}
