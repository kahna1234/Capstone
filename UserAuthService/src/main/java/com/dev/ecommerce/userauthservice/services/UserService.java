package com.dev.ecommerce.userauthservice.services;

import com.dev.ecommerce.userauthservice.models.User;
import com.dev.ecommerce.userauthservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);

        if(optionalUser.isEmpty()){
            throw new RuntimeException("User with id " + id + " not found");
        }

        return optionalUser.get();
    }
}
