package com.example.demo.user.controller;

import com.example.demo.user.dto.*;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpReq request) {
        try {
            userService.signup(request);
            return ResponseEntity.ok("회원가입이 완료되었습니다");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq request) {
        try {
            LoginRes response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetailInfo(@AuthenticationPrincipal String userId){
        try{
            FindDetailRes response = userService.getUserDetailInfo(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editDetailInfo(@AuthenticationPrincipal String userId, @RequestBody EditDetailInfoReq req){
        try{
            EditDetailInfoRes res = userService.editUserDetailInfo(userId, req);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}