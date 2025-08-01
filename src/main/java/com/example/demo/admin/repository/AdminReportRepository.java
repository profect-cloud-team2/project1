package com.example.demo.admin.repository;

import com.example.demo.admin.entity.AdminReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminReportRepository extends JpaRepository<AdminReport, UUID> {
}
