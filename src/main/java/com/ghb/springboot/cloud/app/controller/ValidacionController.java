package com.ghb.springboot.cloud.app.controller;

import java.util.List;

import com.ghb.springboot.cloud.app.service.IConfiguracionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("validacion")
public class ValidacionController {
    
    @Autowired
    private IConfiguracionService configuracionService;

    @PostMapping("/validarRuta")
    public List<String> validarRuta(@RequestBody String root)
    {
        return configuracionService.validarRuta(root);
    }

    @PostMapping("/validarCifrado")
    public List<String> validarCifrado(@RequestBody String root)
    {
        return configuracionService.validarArchivosCifrado(root);
    }
}
