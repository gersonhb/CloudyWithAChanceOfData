package com.ghb.springboot.cloud.app.controller;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Archivo;
import com.ghb.springboot.cloud.app.service.IDirectorioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {
    
    @Autowired
    private IDirectorioService directorioService;
    
    @GetMapping("/bkArchivo/{archivo}")
    public List<Archivo> listarBkArchivos(@PathVariable String archivo)
    {
        return directorioService.listarBkFile(archivo);
    }
}
