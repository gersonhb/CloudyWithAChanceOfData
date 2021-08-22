package com.ghb.springboot.cloud.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ghb.springboot.cloud.app.entity.Archivo;
import com.ghb.springboot.cloud.app.entity.Configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectorioServiceImpl implements IDirectorioService {

    @Autowired
    private IConfiguracionService configuracionService;

    @Override
    public List<Archivo> listarDirectorio(){

        Configuracion configuracion = configuracionService.findByParametro("ROOT");
        List<Archivo> ls=new ArrayList<>();
        Stream<Path> stream=null;

        try {
            stream = Files.list(Paths.get(configuracion.getValor()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ls=stream
        .sorted()
        .filter(n->!(n.getFileName().toString().matches("^[a-zA-Z0-9._]+_\\d\\.[a-zA-Z0-9]+$")))
        .map(n->	
           	new Archivo(
            n.toFile().getName(), 
            n.toFile().length(),
            Instant.ofEpochMilli(
                (n.toFile().lastModified()))
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
            n.toFile().isDirectory()))
        .collect(Collectors.toList());
        stream.close();

        return ls;
    }

	@Override
	public List<Archivo> listarBkFile(String archivo) {
		Configuracion configuracion = configuracionService.findByParametro("ROOT");
        List<Archivo> ls=new ArrayList<>();
        Stream<Path> stream=null;
        String[] parts=archivo.split("\\.");

        try {
            stream = Files.list(Paths.get(configuracion.getValor()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ls=stream
        .sorted()
        .filter(n->n.getFileName().toString().matches("^"+parts[0]+"_\\d\\."+parts[1]+"$"))
        .map(n->	
           	new Archivo(
            n.toFile().getName(), 
            n.toFile().length(),
            Instant.ofEpochMilli(
                (n.toFile().lastModified()))
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
            n.toFile().isDirectory()))
        .collect(Collectors.toList());
        stream.close();

        return ls;
	}

	@Override
	public String mkdir(String directorio) {
		
        Configuracion configuracion=configuracionService.findByParametro("ROOT");
        Path d=Paths.get(configuracion.getValor()+"/"+directorio);
        
        if(directorio.matches("^[a-zA-Z][a-zA-Z0-9.-_ ]{1,15}"))
        {
            if(Files.exists(d))
            {
                return "La carpeta "+directorio+" ya existe";
            }
            else
            {
                try {
                    Files.createDirectory(d);
                    return "Se creó la carpeta "+directorio;
                } catch (IOException e) {
                    return "Error al crear la carpeta "+directorio;
                }
            }
        }
        else
            return "Nombre incorrecto, el nombre no puede empezar con número o caracter especial o espacio en blanco, tamaño máximo de nombre 16 caracteres";
	}
}
