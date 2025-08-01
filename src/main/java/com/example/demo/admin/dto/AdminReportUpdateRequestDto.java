package com.example.demo.admin.dto;

import com.example.demo.admin.entity.ReportStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReportUpdateRequestDto {
	private ReportStatus status;
}
