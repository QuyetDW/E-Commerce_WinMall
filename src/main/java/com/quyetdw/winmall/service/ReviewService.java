package com.quyetdw.winmall.service;

import com.quyetdw.winmall.model.Product;
import com.quyetdw.winmall.model.Review;
import com.quyetdw.winmall.model.User;
import com.quyetdw.winmall.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest request,
                        User user,
                        Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;
    void deleteReview(Long reviewId, Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
}
