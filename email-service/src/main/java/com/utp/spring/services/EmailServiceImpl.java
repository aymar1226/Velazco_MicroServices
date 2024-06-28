package com.utp.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.utp.spring.dto.CorreoDTO;
import com.utp.spring.models.dao.IUsuarioDAO;
import com.utp.spring.models.entity.Usuario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService{
	
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
    private TemplateEngine templateEngine;
	
	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Override
	public void correoVenta(CorreoDTO correoDTO) throws MessagingException {
		// TODO Auto-generated method stub
		String correo = correoDTO.getCorreo();
		Usuario usuario = usuarioDAO.findByEmail(correo).orElseThrow(()-> new RuntimeException("No se encontro el usuario"));
		
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        String mensaje = "Gracias por comprar en Velazco";

        Context context = new Context();
        context.setVariable("nombre", usuario.getPersona().getNombre());
        context.setVariable("mensaje", mensaje);

        String htmlContent = templateEngine.process("email-template", context);

        helper.setTo(correo);
        helper.setSubject("Correo personalizado");
        helper.setText(htmlContent, true);

        emailSender.send(message);
	}

}
