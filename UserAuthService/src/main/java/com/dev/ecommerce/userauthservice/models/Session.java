package com.dev.ecommerce.userauthservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity
public class Session extends BaseModel{
    @Column(columnDefinition = "TEXT")
    private String token;

    @ManyToOne
    private User user;

    private Date expiringAt;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiringAt() {
        return expiringAt;
    }

    public void setExpiringAt(Date expiringAt) {
        this.expiringAt = expiringAt;
    }
}

/*
Session User => M : 1
1        1
M        1
 */
