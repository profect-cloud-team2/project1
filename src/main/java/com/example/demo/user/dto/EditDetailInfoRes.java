package com.example.demo.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditDetailInfoRes {
	private String loginId;

	private String nickname;

	private String email;

	private String phone;

	private String password;

	private String rePassword; //비밀번호 재확인
}
