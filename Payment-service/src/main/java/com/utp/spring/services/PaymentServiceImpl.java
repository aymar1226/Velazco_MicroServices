package com.utp.spring.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.utp.spring.models.dao.ICarritoDao;
import com.utp.spring.models.dao.ICarritoItemDao;
import com.utp.spring.models.dao.IDetalleOrdenDAO;
import com.utp.spring.models.dao.IOrdenDAO;
import com.utp.spring.models.dao.IUsuarioDAO;
import com.utp.spring.models.dto.CarritoDTO;
import com.utp.spring.models.dto.OrdenDTO;
import com.utp.spring.models.dto.PaymentDTO;
import com.utp.spring.models.entity.Carrito;
import com.utp.spring.models.entity.CarritoItem;
import com.utp.spring.models.entity.DetalleOrden;
import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.entity.Usuario;

@Service
public class PaymentServiceImpl implements IPaymentService {

	@Value("${stripe.api.key}")
	private String stripeApiKey;

	@Autowired
	private ICarritoDao carritoDao;

	@Autowired
	private ICarritoItemDao carritoItemDao;

	@Autowired
	private IOrdenDAO ordenDAO;

	@Autowired
	private IDetalleOrdenDAO detalleOrdenDAO;

	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Override
	public PaymentIntent paymentIntent(CarritoDTO carritoDTO) throws StripeException {
		Stripe.apiKey = stripeApiKey;

		// Convertir el total del carrito (double) a centavos (long)
		int amountInCents = (int) (carritoDTO.getTotal() * 100);

		Map<String, Object> params = new HashMap<>();
		params.put("amount", amountInCents);
		params.put("currency", "pen");
		params.put("description", "Payment for cart id: " + carritoDTO.getId());

		ArrayList<String> payment_method_types = new ArrayList<>();
		payment_method_types.add("card");
		params.put("payment_method_types", payment_method_types);

		return PaymentIntent.create(params);
	}

	@Override
	public Map<Boolean, String> confirm(OrdenDTO ordenDTO) throws StripeException {
		Stripe.apiKey = stripeApiKey;

		String correo = ordenDTO.getCorreo();
		String paymentIntentId = ordenDTO.getPaymentIntentId();

		Map<Boolean, String> verificacion = new HashMap<>();

		try {
			PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
			System.out.println(paymentIntent.getStatus());
			if ("succeeded".equals(paymentIntent.getStatus())) {
				verificacion.put(true, "Pago verificado y orden procesada");

				// Logica de guardar orden
				Carrito carrito = carritoDao.findByEmail(correo)
						.orElseThrow(() -> new RuntimeException("No se pudo encontrar el carrito"));
				Usuario usuario = usuarioDAO.findByEmail(correo)
						.orElseThrow(() -> new RuntimeException("No se pudo encontrar el usuario"));

				Orden orden = new Orden();
				orden.setUsuario(usuario);
				orden.setCodigo(generarNumeroOrden());
				orden.setFecha_venta(LocalDate.now());
				orden.setTotal(carrito.getTotal());
				orden.setEstado('1');
				ordenDAO.save(orden);

				List<CarritoItem> items = carritoItemDao.findByCarrito(carrito.getId());
				for (CarritoItem item : items) {
					DetalleOrden detalleOrden = new DetalleOrden();
					detalleOrden.setTotal(item.getTotal());
					detalleOrden.setCantidad(item.getCantidad());
					detalleOrden.setProducto(item.getProducto());
					detalleOrden.setPrecioxunidad(item.getProducto().getPrecio());
					detalleOrden.setEstado('1');
					detalleOrden.setOrden(orden);
					detalleOrdenDAO.save(detalleOrden);
				}

				// Eliminar los productos comprados del carrito
				carritoItemDao.deleteItemsByCarritoId(carrito.getId());

				return verificacion;
			} else {
				verificacion.put(false, "El pago no se ha completado");
				return verificacion;
			}

		} catch (StripeException e) {
			System.out.println("Error al verificar el pago con Stripe: " + e);
			verificacion.put(false, "Error al procesar el pago" + e);
			return verificacion;
		}

	}

	@Override
	public PaymentIntent cancel(String id) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
		paymentIntent.cancel();
		return paymentIntent;
	}

	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";

		List<Orden> ordenes = ordenDAO.findAll();

		List<Integer> numeros = new ArrayList<Integer>();

		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getCodigo())));

		if (ordenes.isEmpty()) {
			numero = 1;
		} else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}

		if (numero < 10) { // 0000001000
			numeroConcatenado = "000000000" + String.valueOf(numero);
		} else if (numero < 100) {
			numeroConcatenado = "00000000" + String.valueOf(numero);
		} else if (numero < 1000) {
			numeroConcatenado = "0000000" + String.valueOf(numero);
		} else if (numero < 10000) {
			numeroConcatenado = "0000000" + String.valueOf(numero);
		}

		return numeroConcatenado;
	}

}
