package com.utp.spring.services;

import com.utp.spring.dto.CorreoDTO;

import jakarta.mail.MessagingException;

public interface IEmailService {
	public void correoVenta(CorreoDTO correoDTO) throws MessagingException;
}
