package com.dev.ecommerce.userauthservice.models;

import jakarta.persistence.Entity;

@Entity
public class Role extends BaseModel{
    private String name; //Mentor, Instructor, Admin, TA
    /*
    allowed permissions for this role
     */

//    @ManyToMany(mappedBy = "roles")
//    private List<User> users;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
