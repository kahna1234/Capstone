package com.dev.ecommerce.userauthservice.controllers;

import com.dev.ecommerce.userauthservice.dtos.UserDTO;
import com.dev.ecommerce.userauthservice.models.User;
import com.dev.ecommerce.userauthservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id){

        System.out.println("Call from product catalog service");

        User user = userService.getUserById(id);

        return user.convertToUserDTO();
    }
}
