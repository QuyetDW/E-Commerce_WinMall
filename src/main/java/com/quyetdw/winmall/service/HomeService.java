package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Home;
import com.quyetdw.winmall.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}