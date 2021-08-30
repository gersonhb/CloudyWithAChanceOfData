package com.ghb.springboot.cloud.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import com.ghb.springboot.cloud.app.repository.IConfiguracionRepository;
import com.ghb.springboot.cloud.app.repository.IUsuarioRepository;

import org.apache.tomcat.util.http.fileupload.IOUtils;

@Service
public class DatabaseToolsH2 implements IDatabaseTools{

    private IUsuarioRepository usuarioRepository;
    private IConfiguracionRepository configuracionRepository;

    @Autowired
    public DatabaseToolsH2(IUsuarioRepository usuarioRepository, IConfiguracionRepository configuracionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.configuracionRepository = configuracionRepository;
    }
    
    /*@Transactional(readOnly = true)
    @Override
    public ResponseEntity<Object> backupDb() {
        
        ResponseEntity<Object> responseEntity=null;
        try {
            Backup.execute("./backup.zip", "./", "cloud_db", true);
            File file = new File("./backup.zip");
            InputStreamResource resource= new InputStreamResource(new FileInputStream(file));
            
            HttpHeaders headers=new HttpHeaders();

            headers.add("Content-Disposition", 
            String.format("attachment; filename=\"%s\"", file.getName()));
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
    }*/

    @Transactional(readOnly = true)
    @Override
    public void backupDb(HttpServletResponse response) {
        
        try {
            configuracionRepository.backup();
            Path file = Paths.get("./backup.zip");

            response.setContentType("application/txt");
            response.setHeader("Content-disposition", "attachment; filename=" + file.toFile().getName());

            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file.toFile());

            IOUtils.copy(in, out);
            out.close();
            in.close();
            Files.delete(file);
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void restoresDb() {
       //Restore.execute("./"+DatabaseToolsH2.class.getSimpleName() + ".zip", "./", "cloud_db");
    }

    @Transactional(readOnly = true)
    @Override
    public Long sizeDatabase() {
        return usuarioRepository.sizeDatabase();
    }

    @Transactional(readOnly = true)
    @Override
    public String nameDatabase() {
        return usuarioRepository.nameDatabase();
    }
    
}
