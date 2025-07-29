package com.example.demo.favorite.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.favorite.entity.FavoriteEntity;
import com.example.demo.user.entity.UserEntity;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, String> {

	// FavoriteEntity findOneByUserIdAndDeletedAtIsNull(UUID userId);
	FavoriteEntity findByUserUserIdAndDeletedAtIsNull(UUID userId);
}
