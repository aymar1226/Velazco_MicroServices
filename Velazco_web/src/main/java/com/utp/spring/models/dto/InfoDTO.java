package com.utp.spring.models.dto;

import lombok.Data;

@Data
public class InfoDTO {
	private String documento;
	private String nombre;
    private String ap_paterno;
    private String ap_materno;
    private String telefono;
    private String direccion;
    private String correo;
}
