package com.dev.ecommerce.userauthservice.models;

import com.dev.ecommerce.userauthservice.dtos.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class User extends BaseModel{
    private String email;
    private String password;
    private String name;

    @ManyToMany
    private List<Role> roles;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserDTO convertToUserDTO() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(this.email);
        userDto.setName(this.name);
        userDto.setRoles(this.roles);
        userDto.setId(this.getId());
        return userDto;
    }
}
