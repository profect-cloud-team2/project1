package com.example.demo.user.service;

import com.example.demo.global.jwt.AccessTokenProvider;
import com.example.demo.user.dto.EditDetailInfoReq;
import com.example.demo.user.dto.EditDetailInfoRes;
import com.example.demo.user.dto.LoginReq;
import com.example.demo.user.dto.LoginRes;
import com.example.demo.user.dto.FindDetailRes;
import com.example.demo.user.dto.SignUpReq;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.exception.DuplicateEmailException;
import com.example.demo.user.exception.DuplicateLoginIdException;
import com.example.demo.user.exception.DuplicateNicknameException;
import com.example.demo.user.exception.InvalidUserEditRequestException;
import com.example.demo.user.exception.PasswordMismatchException;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.exception.UserNotFoundException;
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
            .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException();
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
            .orElseThrow(UserNotFoundException::new);

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
            .orElseThrow(UserNotFoundException::new);

        isValid(req.getLoginId(), req.getEmail(), req.getNickname(), req.getPassword(), req.getRePassword());

        if (req.getLoginId() != null)
            user.updateLoginId(req.getLoginId());
        if (req.getEmail() != null)
            user.updateEmail(req.getEmail());
        if (req.getNickname() != null)
            user.updateNickname(req.getNickname());
        if (req.getPhone() != null)
            user.updatePhone(req.getPhone());
        if (req.getPassword() != null)
            user.updatePassword(passwordEncoder.encode(req.getPassword()));

        user.updateUpdatedTimestamp(LocalDateTime.now());

        return EditDetailInfoRes.builder()
            .loginId(user.getLoginId())
            .nickname(user.getNickname())
            .phone(user.getPhone())
            .email(user.getEmail())
            .build();
    }

    @Override
    public void logout(String ref) {

    }

    private void isValid(String loginId, String email, String nickName,
        String password, String rePassword) {
        if (loginId == null && email == null && nickName == null && password == null && rePassword == null) {
            throw new InvalidUserEditRequestException();
        }
        if (loginId != null && userRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException();
        }
        if (email != null && userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
        if (nickName != null && userRepository.existsByNickname(nickName)) {
            throw new DuplicateNicknameException();
        }
        if (password != null && rePassword != null && !password.equals(rePassword)) {
            throw new PasswordMismatchException();
        }
    }
}
