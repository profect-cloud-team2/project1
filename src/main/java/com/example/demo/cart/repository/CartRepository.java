package com.example.demo.cart.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.cart.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
}
