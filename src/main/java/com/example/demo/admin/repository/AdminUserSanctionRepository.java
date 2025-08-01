package com.example.demo.admin.repository;

import com.example.demo.admin.entity.AdminUserSanction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdminUserSanctionRepository extends JpaRepository<AdminUserSanction, UUID> {

	// 삭제되지 않은 제재 목록
	List<AdminUserSanction> findAllByDeletedAtIsNull();

}
