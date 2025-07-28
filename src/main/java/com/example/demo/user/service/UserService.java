package com.example.demo.user.service;

import com.example.demo.user.dto.*;
import com.example.demo.user.entity.UserEntity;

public interface UserService {
    UserEntity signup(UserSignUpReq request);
    LoginRes login(LoginReq request);
    void logout(String refreshToken);
    UserDetailRes getUserDetailInfo(String userId);
}
