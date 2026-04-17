package com.dev.ecommerce.userauthservice.dtos;

import com.dev.ecommerce.userauthservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private List<Role> roles;
}
