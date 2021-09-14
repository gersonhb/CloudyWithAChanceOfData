package com.ghb.springboot.cloud.app.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import com.ghb.springboot.cloud.app.entity.Ruta;
import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.repository.IUsuarioRepository;
import com.ghb.springboot.cloud.app.service.IPaginacionService;
import com.ghb.springboot.cloud.app.service.IRutaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supervisor")
@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR','ROLE_SUPERVISOR')")
public class SupervisorController {
    
    private IUsuarioRepository usuarioRepository;
    private IPaginacionService paginacionService;
    private IRutaService rutaService;

    @Autowired
    public SupervisorController(IUsuarioRepository usuarioRepository, IPaginacionService paginacionService,
            IRutaService rutaService) {
        this.usuarioRepository = usuarioRepository;
        this.paginacionService = paginacionService;
        this.rutaService =  rutaService;
    }

    @GetMapping({"","/"})
    public String misRutas(@RequestParam(required = false) Integer page,Model model,Authentication authentication)
    {
        Long idUsuario = usuarioRepository.findByUsername(authentication.getName()).getId();

        int pagina = page!=null? (page-1):0;

        PageRequest pageRequest = PageRequest.of(pagina, 10);
        Page<Ruta> pageRuta= paginacionService.getPropietariosRuta(idUsuario,pageRequest);

        int totalPage = pageRuta.getTotalPages();
        if(totalPage>0)
        {
            List<Integer> paginas = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", paginas);
        }

        model.addAttribute("titulo", "Mis Rutas");
        model.addAttribute("rutas", pageRuta.getContent());
        model.addAttribute("actual",pagina+1);
        model.addAttribute("siguiente",pagina+2);
        model.addAttribute("previo",pagina);
        model.addAttribute("ultimo",totalPage);
        model.addAttribute("correlativo", pagina*10);

        return "supervisor/misRutas";
    }

    @GetMapping("/editarRutas/{dir}")
    public String editarRutas(@PathVariable String dir,Model model)
    {
        String ruta=dir.replaceAll("\\+", "/");
        model.addAttribute("titulo", "Editar Rutas");
        model.addAttribute("actualPag", "Editar Ruta: "+ruta);
        model.addAttribute("ruta", dir);

        return "supervisor/editarRutas";
    }

    @GetMapping("/propietarios/{dir}")
    public String listarPropietarios(@PathVariable String dir,Model model,@RequestParam(required = false) Integer page,
        Principal principal)
    {
        String ruta=dir.replaceAll("\\+", "/");

        int pagina = page!=null? (page-1):0;

        PageRequest pageRequest = PageRequest.of(pagina, 10);
        Page<Usuario> pageUsuarioP= paginacionService.getPropietariosUsuario(ruta,pageRequest);

        int totalPage = pageUsuarioP.getTotalPages();
        if(totalPage>0)
        {
            List<Integer> paginas = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", paginas);
        }

        model.addAttribute("titulo", "Propietarios Rutas");
        model.addAttribute("actualPag", "Propietarios Ruta: "+ruta);
        model.addAttribute("propietarios", pageUsuarioP.getContent());
        model.addAttribute("actual",pagina+1);
        model.addAttribute("siguiente",pagina+2);
        model.addAttribute("previo",pagina);
        model.addAttribute("ultimo",totalPage);
        model.addAttribute("correlativo", pagina*10);
        model.addAttribute("usuario", principal.getName());
        model.addAttribute("dir", dir);

        return "supervisor/propietarios";
    }

    @GetMapping("/miembros/{dir}")
    public String listarMiembros(@PathVariable String dir,Model model,@RequestParam(required = false) Integer page,
        Principal principal)
    {
        String ruta=dir.replaceAll("\\+", "/");

        int pagina = page!=null? (page-1):0;

        PageRequest pageRequest = PageRequest.of(pagina, 10);
        Page<Usuario> pageUsuarioM= paginacionService.getMiembrosUsuario(ruta,pageRequest);

        int totalPage = pageUsuarioM.getTotalPages();
        if(totalPage>0)
        {
            List<Integer> paginas = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("paginas", paginas);
        }

        model.addAttribute("titulo", "Miembros Rutas");
        model.addAttribute("actualPag", "Miembros Ruta: "+ruta);
        model.addAttribute("miembros", pageUsuarioM.getContent());
        model.addAttribute("actual",pagina+1);
        model.addAttribute("siguiente",pagina+2);
        model.addAttribute("previo",pagina);
        model.addAttribute("ultimo",totalPage);
        model.addAttribute("correlativo", pagina*10);
        model.addAttribute("usuario", principal.getName());
        model.addAttribute("dir", dir);

        return "supervisor/miembros";
    }

    @PostMapping("/agregarPropietario")
    public String agregarPropietario(@RequestParam String username,@RequestParam String ruta,
        RedirectAttributes flash)
    {
        String dir=ruta.replaceAll("\\+", "/");

        flash.addFlashAttribute("info", rutaService.agregarPropietarioRuta(username, dir));
        return "redirect:/supervisor/editarRutas/"+ruta;
    }

    @PostMapping("/agregarMiembro")
    public String agregarMiembro(@RequestParam String username,@RequestParam String ruta,
        RedirectAttributes flash)
    {
        String dir=ruta.replaceAll("\\+", "/");

        flash.addFlashAttribute("info", rutaService.agregarMiembroRuta(username, dir));
        return "redirect:/supervisor/editarRutas/"+ruta;
    }
    
    @PostMapping("/eliminarPropietario")
    public String eliminarPropietario(@RequestParam String username,@RequestParam String ruta,
        RedirectAttributes flash)
    {
        String dir=ruta.replaceAll("\\+", "/");

        flash.addFlashAttribute("info", rutaService.eliminarPropietarioRuta(username, dir));
        return "redirect:/supervisor/editarRutas/"+ruta;
    }

    @PostMapping("/eliminarMiembro")
    public String eliminarMiembro(@RequestParam String username,@RequestParam String ruta,
        RedirectAttributes flash)
    {
        String dir=ruta.replaceAll("\\+", "/");

        flash.addFlashAttribute("info", rutaService.eliminarMiembroRuta(username, dir));
        return "redirect:/supervisor/editarRutas/"+ruta;
    }

    @PostMapping("/eliminarRuta")
    public String eliminarRuta(@RequestParam String ruta,RedirectAttributes flash)
    {
        String dir=ruta.replaceAll("\\+", "/");

        flash.addFlashAttribute("info", rutaService.eliminarRuta(dir));
        return "redirect:/supervisor";
    }
}
