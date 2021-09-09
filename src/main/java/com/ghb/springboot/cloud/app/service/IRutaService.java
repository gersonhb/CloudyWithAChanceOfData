package com.ghb.springboot.cloud.app.service;

public interface IRutaService {

    public String agregarPropietarioRuta(String username,String dir);
    public String agregarMiembroRuta(String username,String dir);
    public String eliminarPropietarioRuta(String username,String dir);
    public String eliminarMiembroRuta(String username,String dir);
    public String eliminarRuta(String dir);
    
}
