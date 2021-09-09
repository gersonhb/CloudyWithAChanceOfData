package com.ghb.springboot.cloud.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component(value = "initService")
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
        System.out.println("***************************************************************");
        System.out.println("*                                                             *");
        System.out.println("*  Ya puede ingresar al aplicativo:    http://localhost:"+
        configuracionService.findByParametro("PORT_SERVER").getValor()+"  *");
        System.out.println("*                                                             *");
        System.out.println("***************************************************************");
    }
    
}
