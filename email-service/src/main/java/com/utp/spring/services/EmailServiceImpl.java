package com.utp.spring.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.utp.spring.dto.CorreoDTO;
import com.utp.spring.models.dao.IDetalleOrdenDAO;
import com.utp.spring.models.dao.IOrdenDAO;
import com.utp.spring.models.dao.IUsuarioDAO;
import com.utp.spring.models.entity.DetalleOrden;
import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.entity.Persona;
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
	
	@Autowired
	private IOrdenDAO ordenDAO;
	
	@Autowired
	private IDetalleOrdenDAO detalleOrdenDAO;
	
	
	@RabbitListener(queues = "emailQueue")
    public void consumeEmail(CorreoDTO correoDTO) throws MessagingException, IOException {
        // Lógica para enviar el correo
        correoVenta(correoDTO);
    }

	@Override
	public void correoVenta(CorreoDTO correoDTO) throws MessagingException, IOException {
		// TODO Auto-generated method stub
		String correo = correoDTO.getCorreo();
		Integer idOrden = correoDTO.getIdOrden();
		Orden orden = ordenDAO.findById(idOrden).orElseThrow(()-> new RuntimeException("No se pudo encontrar la orden"));
		
		List<DetalleOrden> detalles = detalleOrdenDAO.findByOrden(idOrden);
		Persona persona = usuarioDAO.getInfoByEmail(correo).orElseThrow(()-> new RuntimeException("No se pudo encontrar la informacion"));
		
	
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        
        LocalDate fecha = orden.getFecha_venta();
        String nombreCompleto = persona.getNombre() + " " + persona.getAp_paterno() + " " + persona.getAp_materno();
        String dni = persona.getDocumento();
        String direccion = persona.getDireccion();
        String codigo = orden.getCodigo();
        
        Context context = new Context();
        
        String firebaseImageUrl = "https://firebasestorage.googleapis.com/v0/b/pdf-generatorqr.appspot.com/o/imagenes%2Flogo_size-removebg-preview.png?alt=media";

        
        context.setVariable("nombreCompleto", nombreCompleto);
        context.setVariable("dni", dni);
        context.setVariable("direccion", direccion);
        context.setVariable("fecha", fecha);
        context.setVariable("codigo", codigo);
        context.setVariable("detalles", detalles);
        context.setVariable("total", orden.getTotal());
        context.setVariable("firebaseImageUrl", firebaseImageUrl);


        String htmlContent = templateEngine.process("email-template", context);

        helper.setTo(correo);
        helper.setSubject("Confirmación de tu pedido #" + codigo + " - VELAZCO");
        helper.setText(htmlContent, true);

        emailSender.send(message);
	}

}
