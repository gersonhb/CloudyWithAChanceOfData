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
                    new Configuracion("IV_FILE", "0"),
                    new Configuracion("BK_FILE","3")));


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

    @Override
    public List<String> validarRuta(String ruta) {
        
        Path dir=Paths.get(ruta);
        
        Boolean existe = Files.exists(dir);
        Boolean esDirectorio = Files.isDirectory(dir);
        Boolean permisoEscritura = Files.isWritable(dir);

        if (existe && esDirectorio && permisoEscritura)
            return List.of("La ruta ingresada es válida","1");
        else
            return List.of("La ruta ingresada no es válida","0");
    }

    @Override
    public List<String> validarArchivosCifrado(String ruta) {
        Path dir=Paths.get(ruta);

        Boolean existe = Files.exists(dir);
        Boolean esArchivo = Files.isRegularFile(dir);
        Boolean permisoLectura = Files.isReadable(dir);

        if (existe && esArchivo && permisoLectura)
            return List.of("La ruta ingresada es válida","1");
        else
            return  List.of("La ruta ingresada no es válida","0");
    }

    @Transactional
    @Override
    public String guardarConfig(String root, String key, String iv, Integer cant) {
        
        List<Configuracion> configuraciones=configuracionRepository.findAll();

        Boolean validacion = validarRuta(root).get(1).equals("1") &&
            validarArchivosCifrado(key).get(1).equals("1") &&
            validarArchivosCifrado(iv).get(1).equals("1") && 
            cant >=1 && cant <=5;
        
        if (validacion)
        {
            Configuracion root_dir = configuracionRepository.findByParametro(configuraciones.get(0).getParametro());
            Configuracion key_dir = configuracionRepository.findByParametro(configuraciones.get(1).getParametro());
            Configuracion iv_dir = configuracionRepository.findByParametro(configuraciones.get(2).getParametro());
            Configuracion bk_cant = configuracionRepository.findByParametro(configuraciones.get(5).getParametro());

            root_dir.setValor(root);
            key_dir.setValor(key);
            iv_dir.setValor(iv);
            bk_cant.setValor(cant.toString());

            configuracionRepository.save(root_dir);
            configuracionRepository.save(key_dir);
            configuracionRepository.save(iv_dir);
            configuracionRepository.save(bk_cant);

            return "Se actualizó los parámetros de configuración";
        }
        else
            return "No se ha podido actualizar los cambios, valide que las rutas escogidas sean válidas y que la cantidad de backup esté entre 1 y 5";
            
    }

    

}
