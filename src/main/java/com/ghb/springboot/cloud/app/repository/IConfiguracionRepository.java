package com.ghb.springboot.cloud.app.repository;

import com.ghb.springboot.cloud.app.entity.Configuracion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfiguracionRepository extends JpaRepository<Configuracion,Long>{

    public Configuracion findByParametro(String parametro);
    
}
