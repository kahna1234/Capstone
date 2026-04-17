package com.dev.ecommerce.userauthservice.services;

import com.dev.ecommerce.userauthservice.models.User;
import com.dev.ecommerce.userauthservice.pojos.UserToken;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IAuthService {
    User signup(String name, String email, String password) throws JsonProcessingException;
    UserToken login(String email, String password);

    Boolean validateToken(String token);
}
