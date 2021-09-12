package com.ghb.springboot.cloud.app.controller;

import java.security.Principal;

import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.service.IUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario/")
public class UsuarioController {
    
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping({"/infoUsuario","","/"})
    public String infoUsuario(Model model, Principal principal)
    {
        Usuario usuario=usuarioService.findByUsername(principal.getName());

        model.addAttribute("titulo", "Mi Cuenta");
        model.addAttribute("usuario", usuario);

        return "usuario/infoUsuario";
    }

    @PostMapping("/cambioPass")
    public String cambiarPassword(@RequestParam String password, Principal principal, RedirectAttributes flash)
    {
        if(password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$"))
        {
            usuarioService.cambiarPassword(principal.getName(), password);
            flash.addFlashAttribute("success", "Se cambió contraseña con éxito");
            return "redirect:/usuario/infoUsuario";
        }

        flash.addFlashAttribute("error", "La contraseña debe tener un tamaño mínimo 8 caracteres y contener al menos una minúscula, mayúscula, dígito "+
            "y caracter especial (!@#$%^&+=)");
        return "redirect:/usuario/infoUsuario";
    }
}
