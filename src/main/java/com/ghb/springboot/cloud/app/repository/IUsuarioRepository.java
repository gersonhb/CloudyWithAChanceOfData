package com.ghb.springboot.cloud.app.repository;

import com.ghb.springboot.cloud.app.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario,Long>{

    public Usuario findByUsername(String username);

    @Query(nativeQuery = true,value = "SELECT (SELECT CAST(`VALUE` AS BIGINT) FROM INFORMATION_SCHEMA.SETTINGS WHERE NAME = 'info.PAGE_COUNT') * (SELECT CAST(`VALUE` AS INTEGER) FROM INFORMATION_SCHEMA.SETTINGS WHERE NAME = 'info.PAGE_SIZE')")
    public Long sizeDatabase();

    @Query(nativeQuery = true, value = "SELECT * FROM INFORMATION_SCHEMA.CATALOGS")
    public String nameDatabase();
    
}
