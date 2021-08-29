package com.ghb.springboot.cloud.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    
    public String subirArchivo(MultipartFile file,String ruta);
    public ResponseEntity<Object> descargasArchivo(String ruta,String archivo);
}
