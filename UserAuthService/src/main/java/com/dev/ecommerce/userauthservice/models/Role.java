package com.dev.ecommerce.userauthservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role extends BaseModel{
    private String name; //Mentor, Instructor, Admin, TA
    /*
    allowed permissions for this role
     */

//    @ManyToMany(mappedBy = "roles")
//    private List<User> users;

}