package com.ghb.springboot.cloud.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import com.ghb.springboot.cloud.app.Validacion.UsuarioValidador;
import com.ghb.springboot.cloud.app.entity.Rol;
import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.service.IPaginacionService;
import com.ghb.springboot.cloud.app.service.IUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administrador")
@SessionAttributes("usuario")
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
public class AdminController {
    
    private IUsuarioService usuarioService;
    private IPaginacionService paginacionService;
    private UsuarioValidador validador;

    @Autowired    
    public AdminController(IUsuarioService usuarioService, IPaginacionService paginacionService,UsuarioValidador validador) {
        this.usuarioService = usuarioService;
        this.paginacionService = paginacionService;
        this.validador = validador;
    }

    @GetMapping({"/",""})
    public String index()
    {
        return "redirect:/administrador/configuracion";
    }

    @GetMapping("/crearUsuario")
    public String vistaCrearUsuario(Model model)
    {
        Usuario usuario=new Usuario();
        model.addAttribute("titulo", "Crear Usuario");
        model.addAttribute("boton", "Crear");
        model.addAttribute("usuario", usuario);

        return "administrador/crearUsuario";
    }

    @PostMapping("/crearUsuario")
    public String crearUsuario(@Valid Usuario usuario,BindingResult result, Model model,RedirectAttributes flash, SessionStatus status)
    {
        String titulo = (usuario.getId() != null) ? "Editar Usuario" : "Crear Usuario";
        String boton = (usuario.getId() != null) ? "Editar" : "Crear";
        String mensajeFlash = (usuario.getId() != null) ? "Usuario editado con éxito" : "Usuario creado con éxito";
        
        if(usuario.getId() == null)
        {
            validador.validate(usuario, result);
            if(result.hasErrors())
            {
                model.addAttribute("titulo", titulo);
                model.addAttribute("boton", boton);
                model.addAttribute("usuario", usuario);
                return "administrador/crearUsuario";
            }

            usuarioService.crearUsuario(usuario);
        }
        else
        {
            if(usuario.getPassword().isEmpty())
            {
                usuarioService.editarUsuario(
                    usuario.getId() ,  
                    usuario.getPassword(),  
                    usuario.getEnabled(), 
                    "ROLE_"+usuario.getRol());
            }
            else
            {
                if(result.hasErrors())
                {
                    model.addAttribute("titulo", titulo);
                    model.addAttribute("boton", boton);
                    model.addAttribute("usuario", usuario);
                    return "administrador/crearUsuario";
                }
                usuarioService.editarUsuario(
                    usuario.getId() ,  
                    usuario.getPassword(),  
                    usuario.getEnabled(), 
                    "ROLE_"+usuario.getRol());
            }
        }
        
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);

        return "redirect:/administrador/listarUsuarios";
    }

    @GetMapping("/listarUsuarios")
    public String vistaListarUsuarios(@RequestParam(required = false) Integer page,Model model)
    {
        int pagina = page!=null? (page-1):0;

        PageRequest pageRequest = PageRequest.of(pagina, 10);
        Page<Usuario> pageUsuario= paginacionService.getAllUsuarios(pageRequest);

        int totalPage = pageUsuario.getTotalPages();
        if(totalPage>0)
        {
            List<Integer> paginas = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", paginas);
        }

        model.addAttribute("titulo", "Lista de Usuarios");
        model.addAttribute("listaUsuarios", pageUsuario.getContent());
        model.addAttribute("actual",pagina+1);
        model.addAttribute("siguiente",pagina+2);
        model.addAttribute("previo",pagina);
        model.addAttribute("ultimo",totalPage);

        return "administrador/listarUsuarios";
    }

    @GetMapping("/crearUsuario/{id}")
    public String editarUsuario(@PathVariable Long id, Model model, RedirectAttributes flash)
    {
        Usuario usuario=null;

        if (id > 0) {
            usuario = usuarioService.findOne(id);
            if (usuario == null) {
                flash.addFlashAttribute("error", "El ID del usuario no existe en la BD");
                return "redirect:/administrador/listarUsuarios";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del usuario no puede ser 0");
            return "redirect:/administrador/listarUsuarios";
        }

        model.addAttribute("titulo", "Editar Usuario");
        model.addAttribute("boton", "Editar");
        usuario.setRol(usuario.getRol().substring(5));
        model.addAttribute("usuario",usuario);

        return "administrador/crearUsuario";
    }

    @GetMapping("/configuracion")
    public String vistaConfiguracion(Model model)
    {
        model.addAttribute("titulo", "Página de configuración");

        return "administrador/configuracion";
    }

    @ModelAttribute("usuarios")
    public List<Usuario> listarUsuarios()
    {
        return usuarioService.listarUsuarios();
    }

    @ModelAttribute("roles")
    public List<String> listarRoles()
    {
        Rol[] rol=Rol.values();
        List<String> roles=new ArrayList<String>();

        for (Rol r : rol) {
            roles.add(r.name());
        }

        return roles;
    }
}
