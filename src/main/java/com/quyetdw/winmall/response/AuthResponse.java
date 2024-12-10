package com.quyetdw.winmall.response;

import com.quyetdw.winmall.domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    public String message;
    public USER_ROLE role;
}
