package com.ghb.springboot.cloud.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import com.ghb.springboot.cloud.app.entity.Configuracion;
import com.ghb.springboot.cloud.app.repository.IConfiguracionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionServiceImpl implements IConfiguracionService {

    @Autowired
    private IConfiguracionRepository configuracionRepository;

    @Transactional
    @Override
    public void initConfiguracion() {
        List<Configuracion> configuracion = configuracionRepository.findAll();

        if (configuracion.size()==0) {
            configuracionRepository.saveAll(List.of(
                    new Configuracion("ROOT", System.getProperty("user.dir")),
                    new Configuracion("KEY_RUTA", ""), 
                    new Configuracion("IV_RUTA", ""),
                    new Configuracion("KEY_FILE", "0"), 
                    new Configuracion("IV_FILE", "0")));

            File directorio = new File(System.getProperty("user.dir")+"/ROOT_CLOUD");
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    updateConfiguracion("ROOT", System.getProperty("user.dir")+"/ROOT_CLOUD");
                } else {
                    System.out.println("Error al crear directorio ROOT_CLOUD");
                }
            }
            else
                updateConfiguracion("ROOT", System.getProperty("user.dir")+"/ROOT_CLOUD");

        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<Configuracion> findAll() {
        return configuracionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Configuracion findByParametro(String parametro) {
        return configuracionRepository.findByParametro(parametro);
    }

    @Override
    public void updateConfiguracion(String parametro, String ruta) {
        Configuracion configuracion = configuracionRepository.findByParametro(parametro);
        configuracion.setValor(ruta);
        configuracionRepository.save(configuracion);

    }

    @Override
    public Long sizeDirectorio() {

        Configuracion configuracion=configuracionRepository.findByParametro("ROOT");
        Path path=Paths.get(configuracion.getValor());
        Long size = 0L;

        try (Stream<Path> walk = Files.walk(path))
        {  
            size = walk
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            System.out.println("Error al obtener tamaño de unidad");
                            return 0L;
                        }
                    })
                    .sum();
  
        } catch (IOException e) {
            System.out.println("Error al obtener tamaño de unidad");
        }

        return size;
    }

}
