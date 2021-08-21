package com.ghb.springboot.cloud.app.service;

import java.io.File;
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

}
