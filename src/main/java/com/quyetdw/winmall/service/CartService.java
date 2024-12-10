package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Cart;
import com.quyetdw.winmall.model.CartItem;
import com.quyetdw.winmall.model.Product;
import com.quyetdw.winmall.model.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product, String size, int quantity);
    public Cart findUserCart(User user);
}
