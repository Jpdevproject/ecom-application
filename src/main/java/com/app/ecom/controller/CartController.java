package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.entity.CartItem;
import com.app.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-Id") String userId, @RequestBody CartItemRequest cartItemRequest){
        if(!cartService.addToCart(userId,cartItemRequest)){
            return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
        }
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteFromCart(@RequestHeader("X-User-Id") String userId,@PathVariable Long productId){
        boolean deleted=cartService.deleteFromCart(userId,productId);
        return deleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> fetchItemsById(@RequestHeader("X-User-Id") String userId){
        return ResponseEntity.ok(cartService.fetchById(userId));
    }
}
