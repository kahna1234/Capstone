package com.dev.ecommerce.userauthservice.models;

import com.dev.ecommerce.userauthservice.dtos.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel{
    private String email;
    private String password;
    private String name;

    @ManyToMany
    private List<Role> roles;

    public UserDTO convertToUserDTO() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(this.email);
        userDto.setName(this.name);
        userDto.setRoles(this.roles);
        userDto.setId(this.getId());
        return userDto;
    }
}
