package com.ghb.springboot.cloud.app.service;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Configuracion;

public interface IConfiguracionService {

    public void initConfiguracion();
    public List<Configuracion> findAll();
    
}
