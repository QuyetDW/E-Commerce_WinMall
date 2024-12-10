package com.quyetdw.winmall.controller;

import com.quyetdw.winmall.domain.USER_ROLE;
import com.quyetdw.winmall.repository.UserRepository;
import com.quyetdw.winmall.request.LoginOtpRequest;
import com.quyetdw.winmall.request.LoginRequest;
import com.quyetdw.winmall.response.ApiResponse;
import com.quyetdw.winmall.response.AuthResponse;
import com.quyetdw.winmall.response.SignupRequest;
import com.quyetdw.winmall.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception {

        String jwt = authService.createUser(request);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Đăng ký thành công!");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sentOtp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest request) throws Exception {

        authService.sentLoginOtp(request.getEmail(), request.getRole());

        ApiResponse response = new ApiResponse();
        response.setMessage("Gửi mã OTP thành công!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {

        AuthResponse authResponse = authService.signing(request);

        return ResponseEntity.ok(authResponse);
    }
}
