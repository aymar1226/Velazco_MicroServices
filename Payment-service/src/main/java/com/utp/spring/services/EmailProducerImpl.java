package com.utp.spring.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import com.utp.spring.models.dto.CorreoDTO;

@Service
public class EmailProducerImpl implements IEmailProducer{
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
	

	@Override
	public void sendEmailToQueue(CorreoDTO correoDTO) {
		// TODO Auto-generated method stub
        rabbitTemplate.convertAndSend("emailQueue", correoDTO);
	}

}
