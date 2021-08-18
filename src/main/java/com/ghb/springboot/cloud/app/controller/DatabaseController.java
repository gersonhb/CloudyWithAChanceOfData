package com.ghb.springboot.cloud.app.controller;

import com.ghb.springboot.cloud.app.service.IDatabaseTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("administrador/database")
public class DatabaseController {
    
    private IDatabaseTools databaseTools;

    @Autowired
    public DatabaseController(IDatabaseTools databaseTools) {
        this.databaseTools = databaseTools;
    }

    @GetMapping("/")
    public String configDatabase(Model model)
    {
        model.addAttribute("titulo", "Herramientas de Base de Datos");

        return "database/configDatabase";
    }

    @GetMapping("/backup")
    public ResponseEntity<Object> crearBackupDb()
    {
        return databaseTools.backupDb();
    }

    @ModelAttribute("sizeDatabase")
    public String sizeDatabase()
    {
        String size;
        if(databaseTools.sizeDatabase()>=1024 && databaseTools.sizeDatabase()<1048576)
            size=databaseTools.sizeDatabase()/1024+" KB";
        else if(databaseTools.sizeDatabase()>=1048576 && databaseTools.sizeDatabase()<1073741824)
            size=databaseTools.sizeDatabase()/1048576+" MB";
        else if(databaseTools.sizeDatabase()>=1073741824)
            size=databaseTools.sizeDatabase()/1073741824+" GB";
        else
            return databaseTools.sizeDatabase().toString()+" B";

        return size;
    }

    @ModelAttribute("nameDatabase")
    public String nameDatabase()
    {
        return databaseTools.nameDatabase();
    }
    
}
