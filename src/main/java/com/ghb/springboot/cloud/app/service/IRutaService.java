package com.ghb.springboot.cloud.app.service;

import com.ghb.springboot.cloud.app.entity.Ruta;
import com.ghb.springboot.cloud.app.entity.Usuario;

public interface IRutaService {

    public String agregarPropietarioRuta(String username,String dir);
    public String agregarMiembroRuta(String username,String dir);
    public String eliminarPropietarioRuta(String username,String dir);
    public String eliminarMiembroRuta(String username,String dir);
    public String eliminarRuta(String dir);
    public int permisoRutaAnterior(Ruta ruta,Usuario usuario);
    public String rutaAnterior(Ruta ruta);
    public Boolean soyPropietario(String ruta,String usuario);
    
}
