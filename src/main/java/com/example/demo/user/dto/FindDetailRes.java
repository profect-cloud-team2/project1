package com.example.demo.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindDetailRes {
	private String name;
	private String birthdate;
	private String phone;
	private String email;
	private String loginId;
}
