package com.example.demo.favorite.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.favorite.entity.FavoriteEntity;
import com.example.demo.user.entity.UserEntity;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, UUID> {
	// 찜한 가게 1건 조회
	FavoriteEntity findByUserUserIdAndDeletedAtIsNull(UUID userId);

	// 찜한 가게 목록 조회 (페이징)
	// Page<FavoriteEntity> findAllByUserUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);
	// COALESCE로 마지막 변경시간 기준 정렬
	@Query("""
        SELECT f
          FROM FavoriteEntity f
         WHERE f.user.userId    = :userId
           AND f.deletedAt IS NULL
         ORDER BY COALESCE(f.updatedAt, f.createdAt) DESC
    """)
	Page<FavoriteEntity> findAllByUserOrderByLastModifiedDesc(
		@Param("userId") UUID userId,
		Pageable pageable
	);

	Optional<FavoriteEntity> findByUserUserIdAndStoreStoreId(
		UUID userId,
		UUID storeId
	);

	/**
	 * 특정 가게(storeId)를 찜한 총 개수(삭제되지 않은 것만)
	 */
	long countByStoreStoreIdAndDeletedAtIsNull(
		UUID storeId
	);


}
