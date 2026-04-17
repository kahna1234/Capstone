package com.dev.ecommerce.userauthservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Session extends BaseModel{
    @Column(columnDefinition = "TEXT")
    private String token;

    @ManyToOne
    private User user;

    private Date expiringAt;
}

/*
Session User => M : 1
1        1
M        1
 */
