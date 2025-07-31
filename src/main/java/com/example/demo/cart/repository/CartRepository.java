package com.example.demo.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.cart.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
	@Query("SELECT c FROM CartEntity c LEFT JOIN FETCH c.items WHERE c.user.userId = :userId")
	Optional<CartEntity> findWithItemsByUserId(@Param("userId") UUID userId);

}
