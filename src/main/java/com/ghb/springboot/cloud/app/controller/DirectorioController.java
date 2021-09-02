package com.ghb.springboot.cloud.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ghb.springboot.cloud.app.entity.Archivo;
import com.ghb.springboot.cloud.app.service.IConfiguracionService;
import com.ghb.springboot.cloud.app.service.IDirectorioService;
import com.ghb.springboot.cloud.app.service.IFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/directorio")
public class DirectorioController implements HandlerExceptionResolver{

    private IDirectorioService directorioService;
    private IFileService fileService;
    private IConfiguracionService configuracionService;

    @Autowired    
    public DirectorioController(IDirectorioService directorioService,
    IFileService fileService,IConfiguracionService configuracionService) {
        this.directorioService = directorioService;
        this.fileService = fileService;
        this.configuracionService = configuracionService;
    }

    @GetMapping({"/",""})
    public String vistaDirectorio(Model model)
    {
        List<Archivo> directorios=directorioService.listarDirectorio("");
        
        model.addAttribute("titulo", "Directorio Compartido");
        model.addAttribute("directorios", directorios);
        model.addAttribute("ruta", "");
        
        return "directorio/directorio";
    }

    @PostMapping("/upload")
    public String subirArchivo(@RequestParam MultipartFile file,RedirectAttributes flash,@RequestParam String ruta)
    {
        flash.addFlashAttribute("info",fileService.subirArchivo(file,ruta));

        return "redirect:/directorio";
    }

    @PostMapping("/mkdir")
    public String mkdir(@RequestParam String directorio,@RequestParam String ruta, RedirectAttributes flash)
    {
        flash.addFlashAttribute("info", directorioService.mkdir(directorio,ruta));

        return "redirect:/directorio";
    }

    @GetMapping("/descarga/{file}")
    public void descargaArchivo(@PathVariable String file,HttpServletResponse response)
    {
        String[] parts=file.split("=");
        if(parts.length==1)
            fileService.descargasArchivo("/",file,response);
        else
            fileService.descargasArchivo("/"+parts[0].replaceAll("\\+", "/")+"/",parts[1],response);

    }

    @GetMapping("/bkArchivo/{archivo}")
    public String listarBkArchivos(@PathVariable String archivo, Model model)
    {
        String[] parts=archivo.split("=");
        List<Archivo> archivos=null;

        if(parts.length==1)
        {    
            archivos = directorioService.listarBkFile("/",archivo);
            model.addAttribute("ruta","");
        }
        else
        {
            archivos = directorioService.listarBkFile("/"+parts[0].replaceAll("\\+", "/"),parts[1]);
            model.addAttribute("ruta",parts[0].replaceAll("\\+", "/"));
        }
            
        
        model.addAttribute("archivos",archivos);

        return "directorio/bkArchivoTabla";
    }

    @GetMapping("/{dir}")
    public String vistaSubDirectorio(@PathVariable String dir, Model model) {
        
        List<Archivo> directorios=directorioService.listarDirectorio(dir.replaceAll("\\+", "/"));
        model.addAttribute("titulo", "Directorio Compartido");
        model.addAttribute("directorios", directorios);
        model.addAttribute("ruta", dir.replaceAll("\\+", "/"));

        return "directorio/directorio";
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, 
        HttpServletResponse response, 
        Object handler,
        Exception ex)
    {
        ModelAndView modelAndView = new ModelAndView("directorio/directorio");
        if (ex instanceof MaxUploadSizeExceededException)
        {
            List<Archivo> directorios=directorioService.listarDirectorio("");
        
            modelAndView.getModelMap().addAttribute("titulo", "Directorio Compartido");
            modelAndView.getModelMap().addAttribute("directorios", directorios);
            modelAndView.getModelMap().addAttribute("ruta", "");
            modelAndView.getModelMap().addAttribute("error", "El tamaño máximo establecido es "+
                configuracionService.tamanoFileUpload()+" MB");
        }
            
        return modelAndView;
    }
    
    
}
