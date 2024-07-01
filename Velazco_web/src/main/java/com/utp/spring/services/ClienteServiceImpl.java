package com.utp.spring.services;

import com.utp.spring.models.dao.IClienteDAO;
import com.utp.spring.models.dao.IPersonaDao;
import com.utp.spring.models.dto.InfoDTO;
import com.utp.spring.models.entity.Cliente;
import com.utp.spring.models.entity.Persona;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDAO clienteDAO;
    
    @Autowired
    private IPersonaDao personaDao;

    @Override
    public List<Cliente> findAll() {
        return clienteDAO.findAll();
    }

    @Override
    public Cliente save(Cliente cliente) {
        clienteDAO.save(cliente);
        return cliente;
    }

    @Override
    public Cliente findbyId(Long id) {
        return clienteDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Cliente cliente) {
        clienteDAO.delete(cliente);
    }

    @Override
    public Boolean existsByDNI(String dni) {
        return false;
    }

	@Override
	public InfoDTO getInfo(String correo) {
		
		Persona persona = personaDao.getInfoByEmail(correo).orElseThrow(()-> new RuntimeException("No se pudo encontrar la informacion"));
		
		InfoDTO info = new InfoDTO();
		info.setAp_materno(persona.getAp_materno());
		info.setAp_paterno(persona.getAp_paterno());
		info.setCorreo(correo);
		info.setDireccion(persona.getDireccion());
		info.setDocumento(persona.getDocumento());
		info.setNombre(persona.getNombre());
		info.setTelefono(persona.getTelefono());
		
		return info;
	}


}
