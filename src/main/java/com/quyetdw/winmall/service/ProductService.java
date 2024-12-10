package com.quyetdw.winmall.service;

import com.quyetdw.winmall.exception.ProductException;
import com.quyetdw.winmall.model.Product;
import com.quyetdw.winmall.model.Seller;
import com.quyetdw.winmall.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest request, Seller seller);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProduct(String query);
    public Page<Product> getAllProducts(
            String category, String brand, String colors, String sizes,
            Integer minPrice, Integer maxPrice, Integer minDiscount,
            String sort, String stock, Integer pageNumber
    );
    List<Product> getProductBySellerId(Long sellerId);
}
