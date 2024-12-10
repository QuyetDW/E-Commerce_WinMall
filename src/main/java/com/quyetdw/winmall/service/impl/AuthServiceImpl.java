package com.quyetdw.winmall.service.impl;

import com.quyetdw.winmall.config.JwtProvider;
import com.quyetdw.winmall.domain.USER_ROLE;
import com.quyetdw.winmall.model.Cart;
import com.quyetdw.winmall.model.Seller;
import com.quyetdw.winmall.model.User;
import com.quyetdw.winmall.model.VerificationCode;
import com.quyetdw.winmall.repository.CartRepository;
import com.quyetdw.winmall.repository.SellerRepository;
import com.quyetdw.winmall.repository.UserRepository;
import com.quyetdw.winmall.repository.VerificationCodeRepository;
import com.quyetdw.winmall.request.LoginRequest;
import com.quyetdw.winmall.response.AuthResponse;
import com.quyetdw.winmall.response.SignupRequest;
import com.quyetdw.winmall.service.AuthService;
import com.quyetdw.winmall.service.EmailService;
import com.quyetdw.winmall.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomerUserServiceImpl customerUserService;
    private final SellerRepository sellerRepository;

    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {
        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_SELLER)) {

                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null){
                    throw new Exception("Không tìm thấy Seller!");
                }
            }else {
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new Exception("User không tồn tại với Email cung cấp!");
                }
            }
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if (isExist != null){
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "WinMall gửi mã OTP Login/Signup";
        String text = "Mã OTP của bạn là - " + otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    @Override
    public String createUser(SignupRequest request) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())){
            throw new Exception("mã OTP sai!");
        }

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null){
            User createUser = new User();
            createUser.setEmail(request.getEmail());
            createUser.setFullName(request.getFullName());
            createUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createUser.setMobile("0326387050");
            createUser.setPassword(passwordEncoder.encode(request.getOtp()));

            user = userRepository.save(createUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest request) {

        String userName = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(userName,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Đăng nhập thành công!");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String userName, String otp) {

        UserDetails userDetails = customerUserService.loadUserByUsername(userName);

        String SELLER_PREFIX = "seller_";
        if (userName.startsWith(SELLER_PREFIX)){
            userName = userName.substring(SELLER_PREFIX.length());
        }

        if (userDetails == null){
            throw new BadCredentialsException("UserName không hợp lệ!");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(userName);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("Mã OTP không hợp lệ!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
