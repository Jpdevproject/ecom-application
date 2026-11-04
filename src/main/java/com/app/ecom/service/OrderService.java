package com.app.ecom.service;

import com.app.ecom.dto.OrderItemResponse;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.entity.*;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItems=cartService.fetchById(userId);
        if(cartItems.isEmpty())
            return Optional.empty();
        Optional<User> optionalUser=userRepository.findById(Long.valueOf(userId));
        if(optionalUser.isEmpty())
            return Optional.empty();

        BigDecimal totalPrice=cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Order order = Order.builder()
                .user(optionalUser.get())
                .orderStatus(OrderStatus.CONFIRMED)
                .totalAmount(totalPrice)
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> OrderItem.builder()
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .order(order)
                        .build())
                .toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);



        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
       return OrderResponse.builder()
               .id(order.getId())
               .status(order.getOrderStatus())
               .createdAt(order.getCreatedAt())
               .updatedAt(order.getUpdatedAt())
               .totalAmount(order.getTotalAmount())
               .items(order.getItems().stream().map(orderItem ->
                   OrderItemResponse.builder()
                           .quantity(orderItem.getQuantity())
                           .productId(orderItem.getProduct().getId())
                           .price(orderItem.getPrice())
                           .subTotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                           .build()
               ).toList()).build();

    }


}
