package com.app.ecom.service;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.entity.Address;
import com.app.ecom.entity.User;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void createUser(UserRequest user) {
        User newUser= mapToUserEntity(user);
        userRepository.save(newUser);
    }

    private User mapToUserEntity(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .address(Optional.ofNullable( mapToAddressEntity(userRequest.getAddress())).get())
                .build();
    }

    private Address mapToAddressEntity(AddressDto addressDto){
        return Address.builder()
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .country(addressDto.getCountry())
                .zipcode(addressDto.getZipcode())
                .build();
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public boolean updateUserById(Long id, UserRequest updatedUser) {
        User updated=mapToUserEntity(updatedUser);
        return userRepository.findById(id)
                .map(existing->{
                    updated.setId(id);
                    userRepository.save(updated);
                    return true;
                }).orElse(false);


    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(String.valueOf(user.getId()))
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(Optional.ofNullable(mapToAddressDto(user.getAddress())).get())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();


    }

    private AddressDto mapToAddressDto(Address address) {
        return AddressDto.builder()
                .state(address.getState())
                .city(address.getCity())
                .street(address.getStreet())
                .country(address.getCountry())
                .zipcode(address.getZipcode())
                .build();
    }
}
