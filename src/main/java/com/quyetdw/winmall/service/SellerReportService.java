package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Seller;
import com.quyetdw.winmall.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
