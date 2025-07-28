package com.example.demo.store.repository;

import com.example.demo.store.entity.StoreEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, String> {
	boolean existsByNameAndAddress1AndAddress2(String name, String address1, String address2);
}
