package com.app.ecom.service;

import com.app.ecom.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final List<User> userList=new ArrayList<>();
    private AtomicLong count=new AtomicLong(0);
    public List<User> fetchAllUsers(){
        return userList;
    }

    public List<User> createUser( User user){
        user.setId(count.incrementAndGet());
        userList.add(user);
        return userList;
    }
    public Optional<User> getUserById(Long id){
       return userList.stream()
               .filter(user -> user.getId()==id)
               .findFirst();

    }

    public boolean updateUserById(Long id,User updatedUser){
     return userList.stream()
             .filter(user->user.getId().equals(id))
             .findFirst()
             .map(existingUser->{
                  existingUser.setFirstName(updatedUser.getFirstName());
                  existingUser.setLastName(updatedUser.getLastName());
                  return true;
             }).orElse(false);

    }

}
