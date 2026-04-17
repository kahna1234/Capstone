package com.dev.ecommerce.emailservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    private Long id;
    private Long userId;
    private String customerEmail;
    private List<OrderItemDto> items;
    private String status;
    private Double totalAmount;
}
