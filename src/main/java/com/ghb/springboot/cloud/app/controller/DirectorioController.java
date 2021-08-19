package com.ghb.springboot.cloud.app.controller;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Archivo;
import com.ghb.springboot.cloud.app.service.IDirectorioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/directorio")
public class DirectorioController {

    @Autowired
    private IDirectorioService directorioService;
    
    @GetMapping({"/",""})
    public String vistaDirectorio(Model model)
    {
        List<Archivo> directorios=directorioService.listarDirectorio();
        
        model.addAttribute("titulo", "Directorio Compartido");
        model.addAttribute("directorios", directorios);
        
        return "directorio";
    }


    
}
