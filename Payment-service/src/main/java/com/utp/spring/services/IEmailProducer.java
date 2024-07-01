package com.utp.spring.services;

import com.utp.spring.models.dto.CorreoDTO;

public interface IEmailProducer {
	public void sendEmailToQueue(CorreoDTO correoDTO);
}
