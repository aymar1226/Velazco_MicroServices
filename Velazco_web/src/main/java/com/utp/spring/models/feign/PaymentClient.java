package com.utp.spring.models.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.utp.spring.models.dto.CarritoDTO;
import com.utp.spring.models.dto.OrdenDTO;
import com.utp.spring.models.dto.PaymentConfirmationRequest;


import jakarta.servlet.http.HttpServletRequest;

@FeignClient(name = "payment-service")
public interface PaymentClient {

	@PostMapping("/api/payment/intent")
	ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody CarritoDTO carritoDTO);

    @PostMapping("/api/payment/confirm")
    ResponseEntity<Map<Boolean, String>> confirmPayment(@RequestBody OrdenDTO ordenDTO);

    @PostMapping("/api/payment/cancel/{id}")
    ResponseEntity<String> cancelPayment(@PathVariable String id);
}
