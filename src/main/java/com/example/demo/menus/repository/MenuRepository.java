package com.example.demo.menus.repository;

import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, UUID> {
    boolean existsByStoreAndName(StoreEntity store, String name);
}
