package com.quyetdw.winmall.repository;

import com.quyetdw.winmall.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
