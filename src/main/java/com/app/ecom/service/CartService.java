package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.entity.CartItem;
import com.app.ecom.entity.Product;
import com.app.ecom.entity.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        Optional<Product> productOptional=productRepository.findById(cartItemRequest.getProductId());
        if(productOptional.isEmpty())
            return false;
        Product product=productOptional.get();
        if(product.getStockQuantity()<cartItemRequest.getQuantity())
            return false;
        Optional<User> userOptional=userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty())
            return false;
        User user=userOptional.get();
        CartItem existingCartItem=cartItemRepository.findByUserAndProduct(user,product);
        if(existingCartItem!=null){
            existingCartItem.setQuantity(existingCartItem.getQuantity()+cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }else{
           CartItem newCartItem= CartItem.builder()
                   .user(user)
                   .product(product)
                   .quantity(cartItemRequest.getQuantity())
                   .price(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())))
                   .build();
           cartItemRepository.save(newCartItem);

        }
        return true;
    }

    public boolean deleteFromCart(String userId, Long productId) {
        Optional<Product> productOptional=productRepository.findById(productId);
        //if(productOptional.isEmpty()) return false;
        Optional<User> userOptional=userRepository.findById(Long.valueOf(userId));
       // if(userOptional.isEmpty()) return false;

//        userOptional.flatMap(user ->
//            productOptional.map(product -> {
//            cartItemRepository.deleteByUserAndProduct(user,product);
//            return true;
//            })
//        );

        if(userOptional.isPresent() && productOptional.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOptional.get(),productOptional.get());
            return true;
        }
        return false;
    }

    public List<CartItem> fetchById(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(ArrayList::new);


    }
    private CartItemResponse mapToCartItemResponse(CartItem cartItem){
        return CartItemResponse.builder()
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }

    public void clearCart(String userId) {
       userRepository.findById(Long.valueOf(userId))
               .ifPresent(cartItemRepository::deleteByUser);

    }
}
