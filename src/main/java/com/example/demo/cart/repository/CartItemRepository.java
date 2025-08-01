package com.example.demo.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.cart.entity.CartItemEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {
	Optional<CartItemEntity> findByCart_CartIdAndMenu_MenuId(UUID cartId, UUID menuId);

}
