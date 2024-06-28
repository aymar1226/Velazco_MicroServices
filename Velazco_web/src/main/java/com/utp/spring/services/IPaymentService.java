package com.utp.spring.services;

import java.util.Map;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.utp.spring.models.dto.CarritoDTO;
import com.utp.spring.models.dto.PaymentDTO;

public interface IPaymentService {
    public PaymentDTO paymentIntent (String correo) throws StripeException;

    public Map<Boolean, String> confirm(String id,String correo) throws StripeException;
    public PaymentIntent cancel(String id) throws StripeException;
}
