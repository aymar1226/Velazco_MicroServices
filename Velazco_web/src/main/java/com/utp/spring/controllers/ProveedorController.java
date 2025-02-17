package com.utp.spring.controllers;

import com.utp.spring.models.dao.IProveedorDAO;
import com.utp.spring.models.entity.*;
import com.utp.spring.services.IProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService proveedorService;

    @GetMapping("/lista")
    public List<Proveedor> listarProveedores() {
        return proveedorService.findAll();
    }

    @PostMapping("/agregar")
    public ResponseEntity<Proveedor> agregarProveedor(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevoProveedor = proveedorService.save(proveedor);
            return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/delete")
    public ResponseEntity deleteProveedor(@RequestBody Proveedor proveedor) {

        try {
            proveedorService.delete(proveedor);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Proveedor> updateProveedor(@RequestBody Proveedor proveedor) {
        System.out.println(proveedor);

        try {
            Proveedor proveedorModificado = proveedorService.update(proveedor);
            return new ResponseEntity<>(proveedorModificado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
