package com.example.demo.menus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long store_id;

    @Column(nullable = false)
    private String name;

    private String img;

    @Column(nullable = false)
    private Integer price;
}
