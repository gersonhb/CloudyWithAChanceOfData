package com.ghb.springboot.cloud.app.controller;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Archivo;
import com.ghb.springboot.cloud.app.service.IDirectorioService;
import com.ghb.springboot.cloud.app.service.IFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/directorio")
public class DirectorioController {

    private IDirectorioService directorioService;
    private IFileService uploadService;
    private IFileService fileService;

    @Autowired    
    public DirectorioController(IDirectorioService directorioService, IFileService uploadService,
    IFileService fileService) {
        this.directorioService = directorioService;
        this.uploadService = uploadService;
        this.fileService = fileService;
    }

    @GetMapping({"/",""})
    public String vistaDirectorio(Model model)
    {
        List<Archivo> directorios=directorioService.listarDirectorio();
        
        model.addAttribute("titulo", "Directorio Compartido");
        model.addAttribute("directorios", directorios);
        
        return "directorio/directorio";
    }

    @PostMapping("/upload")
    public String subirArchivo(@RequestParam MultipartFile file,RedirectAttributes flash)
    {
        flash.addFlashAttribute("info",uploadService.subirArchivo(file));

        return "redirect:/directorio";
    }

    @PostMapping("/mkdir")
    public String crearDirectorio()
    {
        return "";
    }

    @GetMapping("/descarga/{file}")
    public ResponseEntity<Object> acciones(@PathVariable String file)
    {
        return fileService.descargasArchivo(file);        
    }
    
    @GetMapping("/bkArchivo/{archivo}")
    public List<Archivo> listarBkArchivos(@PathVariable String archivo)
    {
        return directorioService.listarBkFile(archivo);
    }
}
