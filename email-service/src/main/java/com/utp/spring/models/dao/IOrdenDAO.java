package com.utp.spring.models.dao;

import com.utp.spring.models.entity.Orden;
import com.utp.spring.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdenDAO extends JpaRepository<Orden, Integer> {
	@Query("SELECT o FROM Orden o WHERE o.usuario.correo = :correo AND o.estado='1' ")
	List<Orden> findByCorreo (@Param("correo") String correo);
}
