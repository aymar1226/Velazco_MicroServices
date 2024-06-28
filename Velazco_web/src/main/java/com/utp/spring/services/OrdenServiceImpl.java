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
	private IDetalleOrdenDAO iDetalleOrdenDAO;

	@Override
	public Orden save(Orden orden) {
		return ordenDAO.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenDAO.findAll();
	}
	// 0000010
	public String generarNumeroOrden() {
		int numero=0;
		String numeroConcatenado="";
		
		List<Orden> ordenes = findAll();
		
		List<Integer> numeros= new ArrayList<Integer>();
		
		ordenes.stream().forEach(o -> numeros.add( Integer.parseInt( o.getCodigo())));
		
		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero<10) { //0000001000
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}		
		
		return numeroConcatenado;
	}



	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenDAO.findByUsuario(usuario);
	}



	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenDAO.findById(id);
	}

	@Override
	public Orden realizarVenta(List<ProductoDTO> productos, Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

}
