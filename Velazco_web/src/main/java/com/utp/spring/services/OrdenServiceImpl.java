package com.utp.spring.services;

import com.utp.spring.models.dao.IDetalleOrdenDAO;
import com.utp.spring.models.dao.IOrdenDAO;
import com.utp.spring.models.dao.IProductoDAO;
import com.utp.spring.models.dto.ProductoDTO;
import com.utp.spring.models.entity.DetalleOrden;
import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.entity.Producto;
import com.utp.spring.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrdenServiceImpl implements IOrdenService {
	
	@Autowired
	private IOrdenDAO ordenDAO;

	@Autowired
	private IProductoDAO productoDAO;
	@Autowired
	private IDetalleOrdenDAO detalleOrdenDAO;

	@Override
	public Orden save(Orden orden) {
		return ordenDAO.save(orden);
	}

	@Override
	public List<DetalleOrden> findByOrden(Integer id) {
		return detalleOrdenDAO.findByOrden(id);
	}
	

	@Override
	public List<Orden> findByCorreo(String correo) {
		return ordenDAO.findByCorreo(correo);
	}



	@Override
	public Orden realizarVenta(List<ProductoDTO> productos, Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteOrdenUsuario(Integer id) {
		Orden orden = ordenDAO.findById(id).orElseThrow(()-> new RuntimeException("No se encontro la orden"));
		orden.setEstado('0');
		ordenDAO.save(orden);
	}

}
