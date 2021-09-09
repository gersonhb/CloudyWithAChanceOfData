package com.ghb.springboot.cloud.app.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    
    public String subirArchivo(MultipartFile file,String ruta);
    public void descargasArchivo(String ruta,String archivo,HttpServletResponse response);
    public String eliminarArchivo(String ruta,String archivo);
}
