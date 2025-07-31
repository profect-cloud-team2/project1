package com.example.demo.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditDetailInfoReq {
	// @Pattern(regexp = "^[A-Za-z0-9]{1,15}$",
	// 	message = "로그인 ID는 영문, 숫자만 허용하며 최대 15자")
	private String loginId;

	// @Pattern(regexp = "^[가-힣A-Za-z0-9]{1,15}$",
		// message = "닉네임은 한글, 영문, 숫자만 허용하며 최대 15자")
	private String nickname;

	// @Email(message = "올바른 이메일 형식이 아닙니다")
	private String email;

	// @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10-11자리 숫자여야 합니다")
	private String phone;

	// @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z!@#$%^&*]{8,15}$",
	// 	message = "비밀번호는 8-15자, 대문자 1개 이상, 특수문자 1개 이상 포함")
	private String password;

	// @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z!@#$%^&*]{8,15}$",
	// 	message = "비밀번호는 8-15자, 대문자 1개 이상, 특수문자 1개 이상 포함")
	private String rePassword; //비밀번호 재확인
}
