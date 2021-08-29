package com.ghb.springboot.cloud.app.controller;

import java.security.Principal;

import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.service.IUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
    
    @Autowired
    private IUsuarioService usuarioService;
    
    @GetMapping({"","/"})
    public String home(Model model, Principal principal)
    {
        Usuario usuario=usuarioService.findByUsername(principal.getName());
        model.addAttribute("titulo", "Bienvenido "+ usuario.getNombre());
        return "index";
    }

    @GetMapping("/login")
    public String getLoginView(@RequestParam(required = false) String error, Model model,Principal principal,
    RedirectAttributes flash)
    {
        if(error!=null)
            model.addAttribute("error", "Credenciales incorrectas, vuelva a intentar");

        if(principal!=null)
        {
            flash.addFlashAttribute("info", "Ya has iniciado sesión");
            return "redirect:/";
        }

        return "login";
    }
}
