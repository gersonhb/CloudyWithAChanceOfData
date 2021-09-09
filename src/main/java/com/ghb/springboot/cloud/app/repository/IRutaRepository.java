package com.ghb.springboot.cloud.app.repository;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Ruta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRutaRepository extends JpaRepository<Ruta,Long>{
    
    public Ruta findByNombre(String nombre);

    @Query("select r from Ruta r left join fetch r.miembros where r.nombre=?1")
    public Ruta findMiembrosRuta(String nombre);

    @Query(value = "select * from rutas r left join ruta_usuario_propietario p "+
    "on r.ruta_id=p.ruta_id left join usuarios u on p.propietarios_usuario_id=u.usuario_id where "+
    "u.usuario_id=:idUsuario",nativeQuery = true)
    public Page<Ruta> findPropietariosRuta(@Param("idUsuario") Long idUsuario,Pageable pageable);

    public List<Ruta> findByNombreStartsWith(String nombre);
}
