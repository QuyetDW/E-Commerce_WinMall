package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Order;
import com.quyetdw.winmall.model.Seller;
import com.quyetdw.winmall.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransaction();
}
