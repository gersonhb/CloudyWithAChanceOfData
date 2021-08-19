package com.ghb.springboot.cloud.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ghb.springboot.cloud.app.entity.Configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements IFileService{

    private IConfiguracionService configuracionService;
    private ICifradoService cifradoService;

    @Autowired
    public FileServiceImpl(IConfiguracionService configuracionService, ICifradoService cifradoService) {
        this.configuracionService = configuracionService;
        this.cifradoService = cifradoService;
    }

    @Override
    public String subirArchivo(MultipartFile file) {
        
        Configuracion root=configuracionService.findByParametro("ROOT");
        Configuracion keyParam=configuracionService.findByParametro("KEY_FILE");
        Configuracion ivParam=configuracionService.findByParametro("IV_FILE");

        if(file.isEmpty())
            return "Seleccina un archivo a subir";

        if(keyParam.getValor().equals("0") || ivParam.getValor().equals("0"))
            return "Para subir archivos el administrador debe generar los archivos KEY y IV.";

        try {
            byte[] bytes=file.getBytes();
            
            Path ruta=Paths.get(root.getValor()+"/"+file.getOriginalFilename());            
            Files.write(ruta,bytes);

            cifradoService.encriptar(root.getValor()+"/"+file.getOriginalFilename());

            return "El archivo "+file.getOriginalFilename()+" se ha subido exitosamente";

        } catch (IOException e) {
            return "Error al subir archivo";
        }
        
    }

    @Override
    public ResponseEntity<Object> descargasArchivo(String archivo) {
        
        Configuracion root=configuracionService.findByParametro("ROOT");

        ResponseEntity<Object> responseEntity=null;
        try {
            
            cifradoService.desencriptar(root.getValor()+"/"+archivo);
            
            File file = new File(root.getValor()+"/"+archivo+".unlock");

            InputStreamResource resource= new InputStreamResource(new FileInputStream(file));
            
            HttpHeaders headers=new HttpHeaders();

            headers.add("Content-Disposition", 
            String.format("attachment; filename=\"%s\"", archivo));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            responseEntity=ResponseEntity.ok().headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/txt")).body(resource);

            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseEntity;
    }
    
}
