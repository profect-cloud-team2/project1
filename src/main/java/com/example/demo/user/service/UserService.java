package com.example.demo.user.service;

import com.example.demo.user.dto.*;
import com.example.demo.user.entity.UserEntity;

public interface UserService {
    UserEntity signup(SignUpReq request);
    LoginRes login(LoginReq request);
    void logout(String refreshToken);
    FindDetailRes getUserDetailInfo(String userId);
    EditDetailInfoRes editUserDetailInfo(String userId, EditDetailInfoReq editDetailInfoReq);
}
