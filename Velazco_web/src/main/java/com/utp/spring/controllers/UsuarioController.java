package com.utp.spring.controllers;

import com.utp.spring.models.dto.ChangePasswordRequest;
import com.utp.spring.models.dto.PersonaUsuarioDTO;
import com.utp.spring.models.dto.RegistroDTO;
import com.utp.spring.models.entity.*;
import com.utp.spring.security.JWTUtils;
import com.utp.spring.services.IClienteService;
import com.utp.spring.services.IPersonaService;
import com.utp.spring.services.IRolService;
import com.utp.spring.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private IRolService rolService;
    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IPersonaService personaService;
    
    @Autowired
    private JWTUtils jwtUtils;

    BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();



    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuariobyId(@PathVariable Long id) {
        return usuarioService.findbyId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/email/{correo}")
    public ResponseEntity<Usuario> getUsuariobyCorreo(@PathVariable String correo) {
        return usuarioService.findByEmail(correo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }




    @PostMapping("/registrar")
    public  ResponseEntity<Persona> registrarUsuario(@RequestBody RegistroDTO registroDTO){
        System.out.println(registroDTO);

        try {
            Persona nuevaPersona = personaService.save(registroDTO);
            return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/lista")
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }


    @GetMapping("/privilegio/lista")
    public List<Privilegio> listarPrivilegios() {
        return rolService.findAll();
    }

    @GetMapping("/privilegio/{correo}")
    public ResponseEntity<Privilegio> getPrivilegiobyCorreo(@PathVariable String correo) {
        return usuarioService.findPrivilegioByEmail(correo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/cambioprivilegio")
    public  ResponseEntity<Usuario> cambiarPrivilegio(@RequestBody PersonaUsuarioDTO personaUsuarioDTO){
        System.out.println(personaUsuarioDTO);

        try {
            Usuario usuarioModifcado = usuarioService.cambiarPrivilegio(personaUsuarioDTO);
            return new ResponseEntity<>(usuarioModifcado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete")
    public ResponseEntity deleteUsuario(@RequestBody Usuario usuario) {
        System.out.println(usuario);
        try {
            usuarioService.delete(usuario);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/cambio-contrasena")
    public ResponseEntity<Boolean> changePassword(HttpServletRequest request, @RequestBody ChangePasswordRequest changePasswordRequest) {
    	
        String correo = jwtUtils.extractEmailFromRequest(request);
        
        boolean result = usuarioService.changePassword(correo, changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        
        if (result) {
            return ResponseEntity.ok(result); // Corrección en la sintaxis
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

}



