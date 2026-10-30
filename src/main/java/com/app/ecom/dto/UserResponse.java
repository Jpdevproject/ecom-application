package com.app.ecom.dto;

import com.app.ecom.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class UserResponse {

    private  String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role=UserRole.CUSTOMER;

    private AddressDto address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
