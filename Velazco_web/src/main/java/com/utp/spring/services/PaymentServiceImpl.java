package com.utp.spring.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.utp.spring.models.dao.ICarritoDao;
import com.utp.spring.models.dto.CarritoDTO;
import com.utp.spring.models.dto.OrdenDTO;
import com.utp.spring.models.dto.PaymentDTO;
import com.utp.spring.models.entity.Carrito;
import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.feign.PaymentClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements IPaymentService{

    @Autowired
    private ICarritoDao carritoDao;
    
    @Autowired
    private PaymentClient paymentClient;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public PaymentDTO paymentIntent(String correo) throws StripeException {
    	
        	Carrito carrito = carritoDao.findByEmail(correo).orElseThrow(()-> new RuntimeException("No se pudo encontrar el carrito del usuario"));
       
            CarritoDTO carritoDTO = new CarritoDTO();
            carritoDTO.setEmail(correo);
            carritoDTO.setTotal(carrito.getTotal());
            carritoDTO.setId(carrito.getId());

            try {
                ResponseEntity<Map<String, String>> response = paymentClient.createPaymentIntent(carritoDTO);
                
                if (response.getBody() == null || response.getBody().get("clientSecret") == null || response.getBody().get("paymentIntentId") == null) {
                    throw new RuntimeException("Respuesta de pago inválida");
                }

                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setClientSecret(response.getBody().get("clientSecret"));
                paymentDTO.setPaymentIntentId(response.getBody().get("paymentIntentId"));
                
                return paymentDTO;
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                // Manejo de errores específicos de HTTP
                throw new RuntimeException("Error en la llamada al servicio de pago: " + e.getStatusCode());
            } catch (Exception e) {
                // Manejo de errores genéricos
                throw new RuntimeException("Error inesperado en la llamada al servicio de pago", e);
            }
     
    }

    @Override
    public Map<Boolean, String> confirm(String id, String correo) throws StripeException {
    	OrdenDTO ordenDTO = new OrdenDTO();
    	ordenDTO.setCorreo(correo);
    	ordenDTO.setPaymentIntentId(id);
    	
    	ResponseEntity<Map<Boolean, String>> response = paymentClient.confirmPayment(ordenDTO);
    	
        Stripe.apiKey = stripeApiKey;
        
        return response.getBody();
    }

    @Override
    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey=stripeApiKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();
        return paymentIntent;
    }


}
