package com.ghb.springboot.cloud.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;

import com.ghb.springboot.cloud.app.repository.IUsuarioRepository;

import org.h2.tools.Backup;
import org.h2.tools.Restore;

@Service
public class DatabaseToolsH2 implements IDatabaseTools{

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Transactional(readOnly = true)
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
    }

    @Transactional
    @Override
    public void restoresDb() {
       Restore.execute("./"+DatabaseToolsH2.class.getSimpleName() + ".zip", "./", "cloud_db");
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
