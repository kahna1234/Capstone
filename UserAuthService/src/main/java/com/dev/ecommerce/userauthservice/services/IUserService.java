package com.dev.ecommerce.userauthservice.services;

import com.dev.ecommerce.userauthservice.models.User;

public interface IUserService {
    public User getUserById(Long id);
}
