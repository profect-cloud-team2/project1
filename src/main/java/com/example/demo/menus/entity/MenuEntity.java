package com.example.demo.menus.entity;

import com.example.demo.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@Entity
@Table(name = "p_menus_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "menu_id", nullable = false)
    private UUID menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",nullable = false, columnDefinition = "UUID")
    private StoreEntity store;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "imgURL", length = 255)
    private String imgURL;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "required_time", nullable = false)
    private Integer requiredTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "isAvailable", nullable = false)
    private MenuStatus isAvailable;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;
}
