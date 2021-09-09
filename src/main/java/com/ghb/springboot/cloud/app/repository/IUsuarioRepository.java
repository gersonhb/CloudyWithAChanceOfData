package com.ghb.springboot.cloud.app.repository;

import com.ghb.springboot.cloud.app.entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario,Long>{

    public Usuario findByUsername(String username);

    @Query(nativeQuery = true,value = "SELECT (SELECT CAST(`VALUE` AS BIGINT) FROM INFORMATION_SCHEMA.SETTINGS WHERE NAME = 'info.PAGE_COUNT') * (SELECT CAST(`VALUE` AS INTEGER) FROM INFORMATION_SCHEMA.SETTINGS WHERE NAME = 'info.PAGE_SIZE')")
    public Long sizeDatabase();

    @Query(nativeQuery = true, value = "SELECT * FROM INFORMATION_SCHEMA.CATALOGS")
    public String nameDatabase();
    
    @Query(value = "select * from usuarios u left join ruta_usuario_propietario p "+
    "on u.usuario_id=p.propietarios_usuario_id left join rutas r on p.ruta_id=r.ruta_id where "+
    "r.nombre=:nomRuta",nativeQuery = true)
    public Page<Usuario> findPropietariosUsuario(@Param("nomRuta") String nomRuta,Pageable pageable);

    @Query(value = "select * from usuarios u left join ruta_usuario_miembro m "+
    "on u.usuario_id=m.miembros_usuario_id left join rutas r on m.ruta_id=r.ruta_id where "+
    "r.nombre=:nomRuta",nativeQuery = true)
    public Page<Usuario> findMiembrosUsuario(@Param("nomRuta") String nomRuta,Pageable pageable);
}
