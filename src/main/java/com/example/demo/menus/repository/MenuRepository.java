package com.example.demo.menus.repository;

import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    boolean existsByStoreAndName(StoreEntity store, String name);
}
