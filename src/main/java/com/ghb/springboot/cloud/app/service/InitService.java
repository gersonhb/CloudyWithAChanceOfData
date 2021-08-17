package com.ghb.springboot.cloud.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitService implements CommandLineRunner{
    
    private IConfiguracionService configuracionService;
    private IUsuarioService usuarioService;

    @Autowired
    public InitService(IConfiguracionService configuracionService, IUsuarioService usuarioService) {
        this.configuracionService = configuracionService;
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {
        configuracionService.initConfiguracion();
        usuarioService.initUsuario();
    }
    
}
