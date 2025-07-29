package com.example.demo.user.service;

import com.example.demo.global.jwt.AccessTokenProvider;
import com.example.demo.user.dto.LoginReq;
import com.example.demo.user.dto.LoginRes;
import com.example.demo.user.dto.UserDetailRes;
import com.example.demo.user.dto.UserSignUpReq;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collection;
import java.util.Collections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity signup(UserSignUpReq request) {
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 로그인 ID입니다");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .birthdate(request.getBirthdate())
                .phone(request.getPhone())
                .email(request.getEmail())
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .createdBy(UUID.randomUUID())
                .role(UserEntity.UserRole.CUSTOMER)
                .build();

        return userRepository.save(user);
    }

    @Override
    public LoginRes login(LoginReq request) {
        UserEntity user = userRepository.findByLoginId(request.getLoginId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUserId().toString(),
            null,
            authorities
        );

        String accessToken = accessTokenProvider.createAccessToken(authentication);
        return new LoginRes(accessToken, "로그인 성공");
    }

    @Override
    public UserDetailRes getUserDetailInfo(String userId){
        UserEntity user = userRepository.findByUserId(UUID.fromString(userId))
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserDetailRes.builder()
            .name(user.getName())
            .birthdate(user.getBirthdate().toString())
            .phone(user.getPhone())
            .email(user.getEmail())
            .loginId(user.getLoginId())
            .build();
    }

    @Override
    public void logout(String ref){

    }
}
