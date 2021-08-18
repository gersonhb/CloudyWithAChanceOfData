package com.ghb.springboot.cloud.app.service;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Configuracion;

public interface IConfiguracionService {

    public void initConfiguracion();
    public List<Configuracion> findAll();
    public Configuracion findByParametro(String parametro);
    public void updateConfiguracion(String parametro,String ruta);
    public Long sizeDirectorio();
    
}
