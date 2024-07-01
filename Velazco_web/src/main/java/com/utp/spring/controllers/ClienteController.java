package com.utp.spring.controllers;

import com.utp.spring.models.dto.InfoDTO;
import com.utp.spring.models.entity.Cliente;
import com.utp.spring.models.entity.Usuario;
import com.utp.spring.security.JWTUtils;
import com.utp.spring.services.IClienteService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    
    @Autowired JWTUtils jwtUtils;


    @GetMapping("/lista")
    public List<Cliente> listarClientes() {
        return clienteService.findAll();
    }
    
    @GetMapping("/info")
    public ResponseEntity<InfoDTO> obtenerInfo(HttpServletRequest request){
    	String correo = jwtUtils.extractEmailFromRequest(request);
    	InfoDTO info = clienteService.getInfo(correo);
    	
		return new ResponseEntity<>(info,HttpStatus.OK);
    }


}
