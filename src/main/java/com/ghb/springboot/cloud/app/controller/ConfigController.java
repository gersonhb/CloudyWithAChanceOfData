package com.ghb.springboot.cloud.app.controller;

import java.io.File;
import java.util.List;

import com.ghb.springboot.cloud.app.entity.Configuracion;
import com.ghb.springboot.cloud.app.service.ICifradoService;
import com.ghb.springboot.cloud.app.service.IConfiguracionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("administrador/configuracion/global")
@SessionAttributes("configuraciones")
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
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
    
    @PostMapping("/guardarConfig")
    public String guardarConfig(@RequestParam String root,
        @RequestParam String key,
        @RequestParam String iv,
        @RequestParam Integer bkfile,
        @RequestParam Integer size,
        @RequestParam Integer time,
        @RequestParam Integer port,
        RedirectAttributes flash)
    {
        flash.addFlashAttribute("info", configuracionService.guardarConfig(root,key,iv,bkfile,size,time,port));
        
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

        if(diskPartition.getFreeSpace()>=1024L && diskPartition.getFreeSpace()<1048576L)
            space=(Math.round(((double)diskPartition.getFreeSpace()/1024L))*100.0)/100.0+" KB";
        else if(diskPartition.getFreeSpace()>=1048576L && diskPartition.getFreeSpace()<1073741824L)
            space=(Math.round(((double)diskPartition.getFreeSpace()/1048576L))*100.0)/100.0+" MB";
        else if(diskPartition.getFreeSpace()>=1073741824L && diskPartition.getFreeSpace()<1099511627776L)
            space=(Math.round(((double)diskPartition.getFreeSpace()/1073741824L))*100.0)/100.0+" GB";
        else if(diskPartition.getFreeSpace()>=1099511627776L)
            space=(Math.round(((double)diskPartition.getFreeSpace()/1099511627776L))*100.0)/100.0+" TB";
        else
            space=diskPartition.getFreeSpace()+" B";

        return space;
    }

    @ModelAttribute("use_disk")
    public String useDisk()
    {
        Long size=configuracionService.sizeDirectorio(); 
        String space="";

        if(size>=1024L && size<1048576L)
            space=(Math.round(((double)size/1024L))*100.0)/100.0+" KB";
        else if(size>=1048576L && size<1073741824L)
            space=(Math.round(((double)size/1048576L))*100.0)/100.0+" MB";
        else if(size>=1073741824L && size<1099511627776L)
            space=(Math.round(((double)size/1073741824L))*100.0)/100.0+" GB";
        else if(size>=1099511627776L)
            space=(Math.round(((double)size/1099511627776L))*100.0)/100.0+" TB";
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
