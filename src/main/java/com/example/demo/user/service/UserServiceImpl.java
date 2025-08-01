package com.example.demo.user.service;

import com.example.demo.global.jwt.AccessTokenProvider;
import com.example.demo.user.dto.EditDetailInfoReq;
import com.example.demo.user.dto.EditDetailInfoRes;
import com.example.demo.user.dto.LoginReq;
import com.example.demo.user.dto.LoginRes;
import com.example.demo.user.dto.FindDetailRes;
import com.example.demo.user.dto.SignUpReq;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity signup(SignUpReq request) {
        isValid(request.getLoginId(), request.getEmail(), request.getNickname(),
            null, null);

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .birthdate(request.getBirthdate())
                .phone(request.getPhone())
                .email(request.getEmail())
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .createdBy(UUID.randomUUID())
                .role(request.getRole())
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
    public FindDetailRes getUserDetailInfo(String userId){
        UserEntity user = userRepository.findByUserIdAndDeletedAtIsNull(UUID.fromString(userId))
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return FindDetailRes.builder()
            .name(user.getName())
            .birthdate(user.getBirthdate().toString())
            .phone(user.getPhone())
            .email(user.getEmail())
            .loginId(user.getLoginId())
            .build();
    }

    @Override
    public EditDetailInfoRes editUserDetailInfo(String userId, EditDetailInfoReq req){
        UserEntity user = userRepository.findByUserIdAndDeletedAtIsNull(UUID.fromString(userId))
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        isValid(req.getLoginId(), req.getEmail(), req.getNickname(), req.getPassword(), req.getRePassword());

        if (req.getLoginId() != null) user.updateLoginId(req.getLoginId());
        if (req.getEmail() != null) user.updateEmail(req.getEmail());
        if (req.getNickname() != null) user.updateNickname(req.getNickname());
        if (req.getPhone() != null) user.updatePhone(req.getPhone());
        if (req.getPassword() != null) user.updatePassword(passwordEncoder.encode(req.getPassword()));

        user.updateUpdatedTimestamp(LocalDateTime.now());

        return EditDetailInfoRes.builder()
            .loginId(user.getLoginId())
            .nickname(user.getNickname())
            .phone(user.getPhone())
            .email(user.getEmail())
            .build();
    }

    @Override
    public void logout(String ref){

    }

    private void isValid(String loginId, String email, String nickName,
        String password, String rePassword){
        if (loginId == null && email == null && nickName == null && password == null && rePassword == null){
            throw new IllegalArgumentException("변경하실 정보를 입력해주세요.");
        }
        if (loginId != null && userRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 로그인 ID입니다");
        }
        if (email != null && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        if (nickName != null && userRepository.existsByNickname(nickName)){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
        }
        if(password != null && rePassword != null && !password.equals(rePassword)){
            throw new IllegalArgumentException("변경할 비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
        }
    }
}
