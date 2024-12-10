package com.quyetdw.winmall.service.impl;

import com.quyetdw.winmall.model.Cart;
import com.quyetdw.winmall.model.Coupon;
import com.quyetdw.winmall.model.User;
import com.quyetdw.winmall.repository.CartRepository;
import com.quyetdw.winmall.repository.CouponRepository;
import com.quyetdw.winmall.repository.UserRepository;
import com.quyetdw.winmall.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    public final CouponRepository couponRepository;
    public final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null){
            throw new Exception("Không có Coupon!");
        }
        if (user.getUsedCoupons().contains(coupon)){
            throw new Exception("Coupon đã được sử dụng!");
        }
        if (orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("Mã giảm phải nhỏ hơn đơn hàng!");
        }
        if (coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate())
                && LocalDate.now().isBefore(coupon.getValidityStartDate())){

            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;

            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);

            return cart;
        }

        throw new Exception("Mã giảm giá không khả dụng!");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);

        if (coupon == null){
            throw new Exception("Không tìm thấy coupon!");
        }
        Cart cart = cartRepository.findByUserId(user.getId());

        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);

        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(()->
                new Exception("Không tìm thấy Coupon!"));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public Coupon creayeCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);

    }
}
