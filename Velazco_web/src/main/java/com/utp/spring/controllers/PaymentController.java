package com.utp.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.utp.spring.models.dto.CarritoDTO;
import com.utp.spring.models.dto.PaymentConfirmationRequest;
import com.utp.spring.models.dto.PaymentDTO;
import com.utp.spring.models.entity.Carrito;
import com.utp.spring.security.JWTUtils;
import com.utp.spring.services.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/intent")
    public ResponseEntity<PaymentDTO> payment(HttpServletRequest request) throws StripeException {
        String correo = jwtUtils.extractEmailFromRequest(request);
        PaymentDTO paymentDTO = paymentService.paymentIntent(correo);
        
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }


    @PostMapping("/confirm/{id}")
    public ResponseEntity<Map<Boolean, String>> confirm(@PathVariable String id, HttpServletRequest request) throws StripeException {
        System.out.println("Confirming payment for intent: " + id);
        String correo = jwtUtils.extractEmailFromRequest(request);

        Map<Boolean, String> verificacion= paymentService.confirm(id,correo);
        
        if (!verificacion.containsKey(true)) {  // Check if 'true' is absent
            return new ResponseEntity<>(verificacion, HttpStatus.BAD_REQUEST); 
        }
        return new ResponseEntity<>(verificacion, HttpStatus.OK);
    }
    

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable String id) throws StripeException {
        PaymentIntent paymentIntent=paymentService.cancel(id);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

}