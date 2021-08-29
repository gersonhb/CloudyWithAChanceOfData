package com.ghb.springboot.cloud.app.controller;

import java.io.File;
import java.util.List;

import com.ghb.springboot.cloud.app.entity.Configuracion;
import com.ghb.springboot.cloud.app.service.ICifradoService;
import com.ghb.springboot.cloud.app.service.IConfiguracionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("administrador/configuracion/global")
@SessionAttributes("configuraciones")
public class ConfigController {
    
    private IConfiguracionService configuracionService;
    private ICifradoService cifradoService;

    @Autowired
    public ConfigController(IConfiguracionService configuracionService, ICifradoService cifradoService) {
        this.configuracionService = configuracionService;
        this.cifradoService = cifradoService;
    }

    @GetMapping({"/",""})
    public String vistaConfigGlobal(Model model) 
    {
        List<Configuracion> configuraciones=configuracionService.findAll();
        model.addAttribute("titulo", "Configuración Global");
        model.addAttribute("configuraciones", configuraciones);
        
        return "configuracion/global";
    }
    
    @GetMapping("/key")
    public String generarKey(RedirectAttributes flash)
    {
        flash.addFlashAttribute("info", cifradoService.guardarKey());

        return "redirect:/administrador/configuracion/global";
    }

    @GetMapping("/iv")
    public String generarIv(RedirectAttributes flash)
    {
        flash.addFlashAttribute("info", cifradoService.guardarIv());

        return "redirect:/administrador/configuracion/global";
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
        File diskPartition = new File(System.getProperty("user.dir")); 
        String space="";

        if(diskPartition.getFreeSpace()>=1024 && diskPartition.getFreeSpace()<1048576)
            space=(diskPartition.getFreeSpace()/1024)+" KB";
        else if(diskPartition.getFreeSpace()>=1048576 && diskPartition.getFreeSpace()<1073741824)
            space=(diskPartition.getFreeSpace()/1048576)+" MB";
        else if(diskPartition.getFreeSpace()>=1073741824)
            space=(diskPartition.getFreeSpace()/1073741824)+" GB";
        else
            space=diskPartition.getFreeSpace()+" B";

        return space;
    }

    @ModelAttribute("use_disk")
    public String useDisk()
    {
        Long size=configuracionService.sizeDirectorio(); 
        String space="";

        if(size>=1024 && size<1048576)
            space=(size/1024)+" KB";
        else if(size>=1048576 && size<1073741824)
            space=(size/1048576)+" MB";
        else if(size>=1073741824)
            space=(size/1073741824)+" GB";
        else
            space=size+" B";

        return space;
    }

    @ModelAttribute("user_dir")
    public String userDir()
    {
        return System.getProperty("user.dir");
    }

}
