package com.ghb.springboot.cloud.app.repository;

import com.ghb.springboot.cloud.app.entity.Configuracion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfiguracionRepository extends JpaRepository<Configuracion,Long>{

    public Configuracion findByParametro(String parametro);

    @Modifying
    @Query(nativeQuery = true,value = "backup to './backup.zip'")
    public void backup();
    
}
