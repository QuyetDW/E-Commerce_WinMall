package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Cart;
import com.quyetdw.winmall.model.Coupon;
import com.quyetdw.winmall.model.User;

import java.util.List;

public interface CouponService {

    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon creayeCoupon(Coupon coupon);
    List<Coupon> findAllCoupon();
    void deleteCoupon(Long id) throws Exception;
}
