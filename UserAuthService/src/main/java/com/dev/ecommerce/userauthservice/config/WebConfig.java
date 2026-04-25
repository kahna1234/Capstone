package com.dev.ecommerce.userauthservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS is now handled globally by the API Gateway to prevent duplicate headers
        // and 403 Forbidden errors when accessing via ngrok or other public URLs.
        /*
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://frontend:3000", "http://host.docker.internal:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
        */
    }
}
