package com.app.ecom.controller;

import com.app.ecom.model.User;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.createUser(user);
        return ResponseEntity.ok("User added successfully");
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build()).getBody();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> createUser(@PathVariable Long id,@RequestBody User user){
        userService.updateUserById(id,user);
        return ResponseEntity.ok("User updated successfully");
    }
}
