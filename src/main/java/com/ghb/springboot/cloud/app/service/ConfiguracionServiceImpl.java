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
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Service
public class ConfiguracionServiceImpl implements IConfiguracionService {

    @Autowired
    private IConfiguracionRepository configuracionRepository;
    
    @Autowired
    private CommonsMultipartResolver commonsMultipartResolver;

    @Autowired
    private TomcatServletWebServerFactory tomcatServletWebServerFactory;

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
                    new Configuracion("BK_FILE","3"),
                    new Configuracion("MAX_FILE_UPLOAD","5"),
                    new Configuracion("SESSION_TIMEOUT","15"),
                    new Configuracion("PORT_SERVER","8080")));

            File directorio=null;
            String root="";
            
            if(System.getProperty("os.name").contains("Windows"))
            {
                root="\\ROOT_CLOUD";
                directorio = new File(System.getProperty("user.dir")+root);
            }
            else
            {
                root="/ROOT_CLOUD";
                directorio = new File(System.getProperty("user.dir")+root);
            }

            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    updateConfiguracion("ROOT", System.getProperty("user.dir")+root);
                } else {
                    System.out.println("Error al crear directorio ROOT_CLOUD");
                }
            }
            else
                updateConfiguracion("ROOT", System.getProperty("user.dir")+root);

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
    public String guardarConfig(String root, String key, String iv, Integer cant,
        Integer size, Integer time, Integer port) {
        
        List<Configuracion> configuraciones=configuracionRepository.findAll();
        String size_db=configuracionRepository.findByParametro(configuraciones.get(6).getParametro()).getValor();
        String port_db=configuracionRepository.findByParametro(configuraciones.get(8).getParametro()).getValor();
        Boolean validacion=null;

        if(key.length()>0 && iv.length()>0)
        {
            validacion = validarRuta(root).get(1).equals("1") && root.length()>0 &&
                validarArchivosCifrado(key).get(1).equals("1") &&
                validarArchivosCifrado(iv).get(1).equals("1") && 
                cant >=1 && cant <=5 &&
                size >=3 && size <=10240 &&
                time >=10 && time <=500 &&
                port >=1024;
        }
        else if(key.length()==0 && iv.length()==0)
        {
            validacion = validarRuta(root).get(1).equals("1") && root.length()>0 &&
                cant >=1 && cant <=5 &&
                size >=3 && size <=10240 &&
                time >=10 && time <=500 &&
                port >=1024;
        }
        else if(key.length()==0)
        {
            validacion = validarRuta(root).get(1).equals("1") && root.length()>0 &&
                validarArchivosCifrado(iv).get(1).equals("1") && 
                cant >=1 && cant <=5 &&
                size >=3 && size <=10240 &&
                time >=10 && time <=500 &&
                port >=1024;
        }
        else
        {
            validacion = validarRuta(root).get(1).equals("1") && root.length()>0 &&
                validarArchivosCifrado(key).get(1).equals("1") &&
                cant >=1 && cant <=5 &&
                size >=3 && size <=10240 &&
                time >=10 && time <=500 &&
                port >=1024;
        }
        
        if (validacion)
        {
            if(key.length()==0)
                updateConfiguracion("KEY_FILE", "0");
            else
                updateConfiguracion("KEY_FILE", "1");   

            if(iv.length()==0)
                updateConfiguracion("IV_FILE", "0");
            else
                updateConfiguracion("IV_FILE", "1");

            updateConfiguracion("ROOT", root);
            updateConfiguracion("KEY_RUTA", key);
            updateConfiguracion("IV_RUTA", iv);
            updateConfiguracion("BK_FILE", cant.toString());
            updateConfiguracion("SESSION_TIMEOUT", time.toString());

            if(size!=Integer.parseInt(size_db))
            {
                updateConfiguracion("MAX_FILE_UPLOAD", size.toString());
                commonsMultipartResolver.setMaxUploadSize(size*1024*1024);
            }

            if(port!=Integer.parseInt(port_db))
            {
                updateConfiguracion("PORT_SERVER", port.toString());
                tomcatServletWebServerFactory.setPort(port);
            }

            return "Se actualizó los parámetros de configuración";
        }
        else
            return "No se ha podido actualizar los cambios, valide que las rutas escogidas sean válidas o los valores de los otros parametros";
            
    }

    @Transactional(readOnly = true)
    @Override
    public Long tamanoFileUpload() {
        return Long.parseLong(
            configuracionRepository.findByParametro("MAX_FILE_UPLOAD").getValor());
    }
}
