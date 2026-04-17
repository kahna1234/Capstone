package com.dev.ecommerce.userauthservice.pojos;

import com.dev.ecommerce.userauthservice.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserToken {
    private User user;
    private String token;
    public UserToken(User user, String token){
        this.user = user;
        this.token = token;
    }
}
