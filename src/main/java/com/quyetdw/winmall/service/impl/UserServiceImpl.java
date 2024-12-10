package com.quyetdw.winmall.service.impl;

import com.quyetdw.winmall.config.JwtProvider;
import com.quyetdw.winmall.model.User;
import com.quyetdw.winmall.repository.UserRepository;
import com.quyetdw.winmall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new Exception("Email: "+email+" chưa đăng ký!");
        }
        return user;
    }
}
