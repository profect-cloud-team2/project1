package com.example.demo.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_user_info"
       // , indexes = {
       //     @Index(name = "idx_login_id", columnList = "login_id"),
       //     @Index(name = "idx_email", columnList = "email")
       // }
	   )
// @Where(clause = "deleted_at IS NULL")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "birthdate", nullable = false)
	private LocalDate birthdate;

	@Pattern(regexp = "^\\d{10,11}$")
	@Column(name = "phone", nullable = false, length = 255)
	private String phone;

	@Email
	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Column(name = "login_id", nullable = false, length = 255)
	private String loginId;

	@JsonIgnore
	// @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*])[A-Za-z!@#$%^&*]{8,15}$",
	// 	message = "비밀번호는 8-15자, 대문자 1개 이상, 특수문자 1개 이상 포함")
	@Column(name = "password", nullable = false)
	private String password;

	// @Pattern(regexp = "^[가-힣A-Za-z0-9]{1,15}$", message = "닉네임은 한글, 영문, 숫자만 허용하며 최대 15자")
	@Column(name = "nickname", nullable = false)
	private String nickname;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by")
	private UUID createdBy;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "updated_by")
	private UUID updatedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "deleted_by")
	private UUID deletedBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Getter
	public enum UserRole {
		ADMIN("관리자"), 
		CUSTOMER("고객"), 
		OWNER("점주");
		
		private final String description;
		
		UserRole(String description) {
			this.description = description;
		}

	}
	@Builder
	public UserEntity(String name, LocalDate birthdate, String phone, String email, String loginId, String password,
					  String nickname, UUID createdBy, UserRole role) {
		this.name = name;
		this.birthdate = birthdate;
		this.phone = phone;
		this.email = email;
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.createdBy = createdBy;
		this.role = role;
	}

	public void updateLoginId(String loginId){
		this.loginId = loginId;
	}
	public void updateEmail(String email){
		this.email = email;
	}
	public void updatePhone(String phone){
		this.phone = phone;
	}
	public void updatePassword(String password){
		this.password = password;
	}
	public void updateNickname(String nickname){
		this.nickname = nickname;
	}
	public void updateUpdatedTimestamp(LocalDateTime updatedAt){
		this.updatedAt = updatedAt;
	}
}
