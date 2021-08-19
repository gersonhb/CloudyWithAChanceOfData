package com.ghb.springboot.cloud.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    
    public String subirArchivo(MultipartFile file);
    public ResponseEntity<Object> descargasArchivo(String archivo);
}
