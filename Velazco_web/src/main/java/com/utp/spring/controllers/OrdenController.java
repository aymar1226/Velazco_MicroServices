    package com.utp.spring.controllers;

import com.utp.spring.models.entity.DetalleOrden;
import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.entity.Producto;
import com.utp.spring.models.entity.Usuario;
import com.utp.spring.security.JWTUtils;
import com.utp.spring.services.IDetalleOrdenService;
import com.utp.spring.services.IOrdenService;
import com.utp.spring.services.IProductoService;
import com.utp.spring.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {
    @Autowired
    private IProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IOrdenService ordenService;

    @Autowired
    private JWTUtils jwtUtils;


    @GetMapping("/lista")
    public ResponseEntity<List<Orden>> listarOrdenesPorUsuario(HttpServletRequest request){
		System.out.println(request);
    	try {
            String correo = jwtUtils.extractEmailFromRequest(request);
            List<Orden> ordenes = ordenService.findByCorreo(correo);
            return new ResponseEntity<>(ordenes,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/detalles/{id}")
    public ResponseEntity<List<DetalleOrden>> listarDetalleOrden(@PathVariable Integer id){
    	try {
    		
            List<DetalleOrden> detalles = ordenService.findByOrden(id);
            return new ResponseEntity<>(detalles,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/delete/usuario/{id}")
    public ResponseEntity<Void> eliminarOrdenUsuario(@PathVariable Integer id){
    	try {
    	ordenService.deleteOrdenUsuario(id);
    	
		return ResponseEntity.ok().build();
    	}catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }
    
}
