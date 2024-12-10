package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Product;
import com.quyetdw.winmall.model.User;
import com.quyetdw.winmall.model.Wishlist;

public interface WishlistService {
    Wishlist createWishList(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
