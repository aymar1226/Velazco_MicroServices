package com.utp.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utp.spring.dto.CorreoDTO;
import com.utp.spring.services.IEmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("api/mail")
public class EmailController {
	
	@Autowired
	private IEmailService emailService;
	
	@PostMapping("/venta")
	ResponseEntity<Void> enviarCorreoVenta(@RequestBody CorreoDTO correoDTO) throws MessagingException{
		emailService.correoVenta(correoDTO);
		return ResponseEntity.ok().build();
	}

}
