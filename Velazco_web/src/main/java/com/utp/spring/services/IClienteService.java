package com.utp.spring.services;

import com.utp.spring.models.dto.InfoDTO;
import com.utp.spring.models.entity.Cliente;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();
    public Cliente save(Cliente cliente);
    public Cliente findbyId(Long id);
    public void delete(Cliente cliente);
    Boolean existsByDNI(String dni);
    
    public InfoDTO getInfo(String correo);

    }
