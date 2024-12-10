package com.quyetdw.winmall.repository;

import com.quyetdw.winmall.model.Cart;
import com.quyetdw.winmall.model.CartItem;
import com.quyetdw.winmall.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
