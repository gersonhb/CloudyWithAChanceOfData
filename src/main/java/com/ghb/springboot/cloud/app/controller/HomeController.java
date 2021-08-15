package com.ghb.springboot.cloud.app.controller;

import java.security.Principal;

import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.service.IUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private IUsuarioService usuarioService;
    
    @GetMapping({"","/"})
    public String home(Model model, Principal principal)
    {
        Usuario usuario=usuarioService.findByUsername(principal.getName());
        model.addAttribute("titulo", "Bienvenido "+ usuario.getNombre()+ " "+usuario.getApellidoPat());
        return "index";
    }

    @GetMapping("/login")
    public String getLoginView(@RequestParam(required = false) String error, Model model)
    {
        if(error!=null)
            model.addAttribute("error", "Credenciales incorrectas, vuelva a intentar");

        return "login";
    }
}
