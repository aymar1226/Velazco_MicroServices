package com.utp.spring.models.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private String paymentIntentId;
    private String clientSecret;
}
