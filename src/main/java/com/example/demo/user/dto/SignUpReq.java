package com.example.demo.user.dto;

import com.example.demo.user.entity.UserEntity;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SignUpReq {
    
    // @NotBlank(message = "이름은 필수입니다")
    private String name;
    
    private LocalDate birthdate;
    
    // @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10-11자리 숫자여야 합니다")
    private String phone;
    
    // @Email(message = "올바른 이메일 형식이 아닙니다")
    // @NotBlank(message = "이메일은 필수입니다")
    private String email;

    // @Pattern(regexp = "^[A-Za-z0-9]{1,15}$",
    //     message = "로그인 ID는 영문, 숫자만 허용하며 최대 15자")
    // @NotBlank(message = "로그인 ID는 필수입니다")
    private String loginId;
    
    // @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z!@#$%^&*]{8,15}$",
    //          message = "비밀번호는 8-15자, 대문자 1개 이상, 특수문자 1개 이상 포함")
    private String password;

    // @Pattern(regexp = "^[가-힣A-Za-z0-9]{1,15}$",
    //          message = "닉네임은 한글, 영문, 숫자만 허용하며 최대 15자")
    private String nickname;

    private UserEntity.UserRole role; //권한
}
