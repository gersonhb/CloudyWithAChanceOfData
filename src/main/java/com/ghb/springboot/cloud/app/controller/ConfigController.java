package com.ghb.springboot.cloud.app.controller;

import java.io.File;
import java.util.List;

import com.ghb.springboot.cloud.app.entity.Configuracion;
import com.ghb.springboot.cloud.app.service.IConfiguracionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("administrador/configuracion/global")
public class ConfigController {
    
    @Autowired
    private IConfiguracionService configuracionService;

    @GetMapping({"/",""})
    public String vistaConfigGlobal(Model model) 
    {
        List<Configuracion> configuraciones=configuracionService.findAll();
        model.addAttribute("titulo", "Configuración Global");
        model.addAttribute("configuraciones", configuraciones);
        
        return "configuracion/global";
    }
    
    @ModelAttribute("os")
    public String sistemaOperativo()
    {
        return System.getProperty("os.name")+" "+System.getProperty("os.arch");
    }

    @ModelAttribute("user_os")
    public String userSistemaOperativo()
    {
        return System.getProperty("user.name");
    }

    @ModelAttribute("free_disk")
    public String freeDisk()
    {
        File diskPartition = new File("/home"); 

        return (diskPartition.getFreeSpace() / (1024 *1024*1024)) + " GB";
    }

}
