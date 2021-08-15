package com.ghb.springboot.cloud.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @GetMapping({"","/"})
    public String home(Model model, Principal principal)
    {
        model.addAttribute("titulo", "Bienvenido "+ principal.getName());
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
