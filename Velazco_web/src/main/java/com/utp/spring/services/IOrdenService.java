package com.utp.spring.services;

import com.utp.spring.models.dto.ProductoDTO;
import com.utp.spring.models.entity.DetalleOrden;
import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IOrdenService {
	public List<Orden> findAll();
	public List<DetalleOrden> findByOrden(Integer id);
	public Orden save (Orden orden);
	public List<Orden> findByCorreo(String correo);
	public Orden realizarVenta(List<ProductoDTO> productos, Usuario usuario);
	public void deleteOrdenUsuario(Integer id);
}
