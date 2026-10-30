package com.app.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@AllArgsConstructor
@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private AddressDto address;
}
