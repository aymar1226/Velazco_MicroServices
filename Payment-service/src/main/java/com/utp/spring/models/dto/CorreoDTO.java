package com.utp.spring.models.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CorreoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
	private String correo;
	private Integer idOrden;
}
