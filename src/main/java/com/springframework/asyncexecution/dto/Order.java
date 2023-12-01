package com.springframework.asyncexecution.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Order{
    private Integer productId;
    private String productName;
    private String productType;
    private int quantity;
    private double price;
    private String trackingId;
}
