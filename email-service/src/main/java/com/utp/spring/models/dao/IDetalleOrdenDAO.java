package com.utp.spring.models.dao;

import com.utp.spring.models.entity.DetalleOrden;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleOrdenDAO extends JpaRepository<DetalleOrden, Integer> {
	@Query("SELECT do FROM DetalleOrden do WHERE do.orden.id = :idOrden")
	List<DetalleOrden> findByOrden (@Param("idOrden") Integer idOrden);
}
