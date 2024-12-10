package com.quyetdw.winmall.service;

import com.quyetdw.winmall.domain.USER_ROLE;
import com.quyetdw.winmall.request.LoginRequest;
import com.quyetdw.winmall.response.AuthResponse;
import com.quyetdw.winmall.response.SignupRequest;

public interface AuthService {
    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signing (LoginRequest request);
}
