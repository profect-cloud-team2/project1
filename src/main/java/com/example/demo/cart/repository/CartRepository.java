package com.example.demo.cart.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.cart.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
	Optional<CartEntity> findByUser_UserIdAndStore_StoreIdAndDeletedAtIsNull(UUID userId, UUID storeId);

	List<CartEntity> findAllByUser_UserIdAndDeletedAtIsNull(UUID userId);

	Optional<CartEntity> findByCartIDAndDeletedAtIsNull(UUID cartId);
}
