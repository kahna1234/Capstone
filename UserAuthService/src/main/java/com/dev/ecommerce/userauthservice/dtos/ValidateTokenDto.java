package com.dev.ecommerce.userauthservice.dtos;

public class ValidateTokenDto {
    private String token;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

/*
{
    scope=[{id=1,
            createdAt=1770998221744,
             lastUpdatedAt=1770998221744,
             state=ACTIVE,
             name=DEFAULT}],
   iss=scaler,
   exp=1771014569731,
   iat=1771004569731,
   userId=3}

 */
