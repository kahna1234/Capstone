package com.dev.ecommerce.userauthservice.configs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
public class AuthConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecretKey secretKey() {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        return  secretKey;
    }

//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(props);
//    }
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }




    //Disable csrf
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable();
//        httpSecurity.authorizeHttpRequests(
//                authorize -> authorize.
//                        anyRequest().
//                        permitAll());
//        return httpSecurity.build();
//    }

}
/*
Extra

When you use Spring security, by default all requests
are blocked due to security filter
All requests should be authenticated to
pass this security filter


CSRF

Cross site request forgery

an attack where an authenticated user is tricked into
executing unwanted actions on a web application

We need protection against attacks like csrf:
Spring security by default might apply,

Spring security:
1. by default, csrf is enabled by
2. POST/PUT/DELETE - 403 response(Forbidden), send
a csrf token

1. REST APIs, it is completely safe to disable csrf
2. stateless apis, safe to disable csrf
3. using jwts / basic auth, it's safe to disable csrf
 */

/*
Secret key programatically

secret keys are stored in vaults

AWS for deployment
Aws provides you key vaults/amazon config store(ACS)
store secret keys in vaults and fetch them when required

Key vaults in MS Azure
ACS in AWS
 */