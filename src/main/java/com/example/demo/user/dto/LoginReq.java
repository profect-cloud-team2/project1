package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {
    
    @NotBlank(message = "로그인 ID는 필수입니다")
    private String loginId;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}