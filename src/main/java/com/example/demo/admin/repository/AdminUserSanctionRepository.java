package com.example.demo.admin.repository;

import com.example.demo.admin.entity.AdminUserSanction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdminUserSanctionRepository extends JpaRepository<AdminUserSanction, UUID> {

	List<AdminUserSanction> findAllByDeletedAtIsNull();

}
