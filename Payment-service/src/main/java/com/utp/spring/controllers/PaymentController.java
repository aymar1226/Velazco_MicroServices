package com.utp.spring.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.utp.spring.models.dto.CarritoDTO;
import com.utp.spring.models.dto.OrdenDTO;
import com.utp.spring.models.dto.PaymentDTO;
import com.utp.spring.services.IPaymentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private IPaymentService paymentService;
    
    @PostMapping("/intent")
    public ResponseEntity<Map<String, String>> payment(@RequestBody CarritoDTO carritoDTO) throws StripeException {
        PaymentIntent paymentIntent = paymentService.paymentIntent(carritoDTO);
        String paymentId = paymentIntent.getId();
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        response.put("paymentIntentId", paymentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<Boolean, String>> confirm(@RequestBody OrdenDTO ordenDTO) throws StripeException {
    	Map<Boolean, String> verificacion = paymentService.confirm(ordenDTO);
    	
    	return new ResponseEntity<>(verificacion, HttpStatus.OK);
    }

    //@PostMapping("/cancel/{id}")
    //public ResponseEntity<String> cancel(@PathVariable String id) throws StripeException {
        // Implementación actual
    //}
}
