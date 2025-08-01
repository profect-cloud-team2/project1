package com.example.demo.user.repository;

import com.example.demo.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickName);
    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByUserIdAndDeletedAtIsNull(UUID userId);
}
