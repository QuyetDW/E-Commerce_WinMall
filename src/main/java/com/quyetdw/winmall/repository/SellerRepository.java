package com.quyetdw.winmall.repository;

import com.quyetdw.winmall.domain.AccountStatus;
import com.quyetdw.winmall.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
