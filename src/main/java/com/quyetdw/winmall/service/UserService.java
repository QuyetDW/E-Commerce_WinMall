package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
