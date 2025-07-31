package com.example.demo.menus.entity;

import lombok.Getter;

@Getter
public enum MenuStatus {
    ONSALE("판매중"),
    SOLDOUT("품절"),
    DISABLE("비활성화");

    private final String description;

    MenuStatus(String description) {
        this.description = description;
    }
}
