package com.example.demo.admin.service;

import com.example.demo.admin.dto.AdminUserSanctionRequestDto;
import com.example.demo.admin.dto.AdminUserSanctionResponseDto;
import com.example.demo.admin.entity.AdminUserSanction;
import com.example.demo.admin.exception.SanctionNotFoundException;
import com.example.demo.admin.repository.AdminUserSanctionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserSanctionService {

	private final AdminUserSanctionRepository repository;

	public List<AdminUserSanctionResponseDto> getAllSanctions() {
		return repository.findAllByDeletedAtIsNull()
			.stream()
			.map(AdminUserSanctionResponseDto::from)
			.toList();
	}

	public AdminUserSanctionResponseDto createSanction(AdminUserSanctionRequestDto dto, UUID adminId) {
		LocalDateTime startDate = dto.getStartDate();

		LocalDateTime endDate = switch (dto.getSanctionStatus()) {
			case WARNING, BAN -> null;
			case SUSPEND -> dto.getEndDate();
		};

		AdminUserSanction sanction = AdminUserSanction.builder()
			.sanctionId(null)
			.userId(dto.getUserId())
			.reason(dto.getReason())
			.sanctionStatus(dto.getSanctionStatus())
			.note(dto.getNote())
			.startDate(startDate)
			.endDate(endDate)
			.createdBy(adminId)
			.build();

		return AdminUserSanctionResponseDto.from(repository.save(sanction));
	}

	public void deleteSanction(UUID sanctionId, UUID deletedBy) {
		AdminUserSanction sanction = repository.findById(sanctionId)
			.orElseThrow(SanctionNotFoundException::new);

		sanction.softDelete(deletedBy);
		repository.save(sanction);
	}
}
