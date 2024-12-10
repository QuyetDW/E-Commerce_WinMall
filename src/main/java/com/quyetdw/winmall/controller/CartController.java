package com.quyetdw.winmall.controller;

import com.quyetdw.winmall.exception.ProductException;
import com.quyetdw.winmall.model.Cart;
import com.quyetdw.winmall.model.CartItem;
import com.quyetdw.winmall.model.Product;
import com.quyetdw.winmall.model.User;
import com.quyetdw.winmall.request.AddItemRequest;
import com.quyetdw.winmall.response.ApiResponse;
import com.quyetdw.winmall.service.CartItemService;
import com.quyetdw.winmall.service.CartService;
import com.quyetdw.winmall.service.ProductService;
import com.quyetdw.winmall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception{

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest request,
                                                  @RequestHeader("Authorization") String jwt) throws ProductException, Exception {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(request.getProductId());

        CartItem item = cartService.addCartItem(user, product, request.getSize(), request.getQuantity());
        ApiResponse response = new ApiResponse();
        response.setMessage("Thêm sản phẩm vào giỏ hàng thành công!");

        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandle(@PathVariable Long cartItemId,
                                                            @RequestHeader("Authorization") String jwt) throws Exception{

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse response = new ApiResponse();
        response.setMessage("Xóa sản phẩm khỏi giỏ hàng thành công!");

        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandle(@PathVariable Long cartItemId,
                                                         @RequestBody CartItem cartItem,
                                                         @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);

        CartItem updateCartItem = null;
        if (cartItem.getQuantity() > 0){
            updateCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }
        return new ResponseEntity<>(updateCartItem, HttpStatus.ACCEPTED);
    }
}
