package com.ghb.springboot.cloud.app.service;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Archivo;

public interface IDirectorioService {

    public List<Archivo> listarDirectorio(String dir);
    public List<Archivo> listarBkFile(String ruta,String archivo);
    public String mkdir(String directorio);
    
}
