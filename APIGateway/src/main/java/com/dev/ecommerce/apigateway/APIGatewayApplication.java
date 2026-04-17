package com.dev.ecommerce.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
        excludeName = {
                "org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration",
                "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration",
                "org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration",

                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                "org.springframework.cloud.autoconfigure.RefreshAutoConfiguration"
        }
)
@EnableDiscoveryClient
public class APIGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(APIGatewayApplication.class, args);
    }
}

