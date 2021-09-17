package com.ghb.springboot.cloud.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import com.ghb.springboot.cloud.app.entity.Ruta;
import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.repository.IRutaRepository;
import com.ghb.springboot.cloud.app.repository.IUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RutaServiceImpl implements IRutaService{

    private IUsuarioRepository usuarioRepository;
    private IRutaRepository rutaRepository;
    private IConfiguracionService configuracionService;

    @Autowired
    public RutaServiceImpl(IUsuarioRepository usuarioRepository, IRutaRepository rutaRepository,
            IConfiguracionService configuracionService) {
        this.usuarioRepository = usuarioRepository;
        this.rutaRepository = rutaRepository;
        this.configuracionService = configuracionService;
    }

    @Override
    public String agregarPropietarioRuta(String username,String dir) {
        Usuario usuario=usuarioRepository.findByUsername(username);

        if(usuario==null)
            return "El username ingresado no existe";
        
        if(usuario.getRol().equals("ROLE_ADMINISTRADOR")||usuario.getRol().equals("ROLE_SUPERVISOR"))
        {
            Ruta ruta=rutaRepository.findByNombre(dir);
            if(ruta.getPropietarios().contains(usuario))
                return "El usuario ya es propietario de esta ruta. No puedes volver a agregarlo";

            ruta.agregarPropietario(usuario);
            permisoRutaAnterior(ruta, usuario);
            rutaRepository.save(ruta);

            return "Se agregó un nuevo propietario.";
        }
        else
            return "El usuario no tiene el perfil administrador o supervisor";
    }

    @Override
    public String agregarMiembroRuta(String username,String dir) {
        Usuario usuario=usuarioRepository.findByUsername(username);

        if(usuario==null)
            return "El username ingresado no existe";

        Ruta ruta=rutaRepository.findByNombre(dir);
        if(ruta.getMiembros().contains(usuario))
            return "El usuario ya es miembro de esta ruta. No puedes volver a agregarlo";

        permisoRutaAnterior(ruta, usuario);

        return "Se agregó un nuevo miembro";
    }

    @Override
    public String eliminarPropietarioRuta(String username, String dir) {
        Usuario usuario=usuarioRepository.findByUsername(username);
        Ruta ruta=rutaRepository.findByNombre(dir);

        ruta.borrarPropietario(usuario);
        rutaRepository.save(ruta);

        return "El usuario "+usuario.getUsername()+" fue eliminado como propietario de la ruta.";
    }

    @Override
    public String eliminarMiembroRuta(String username, String dir) {
        Usuario usuario=usuarioRepository.findByUsername(username);
        Ruta ruta=rutaRepository.findByNombre(dir);
        //String separador= System.getProperty("os.name").contains("Windows")?"\\":"/";

        ruta.borrarPropietario(usuario);
        ruta.borrarMiembro(usuario);
        rutaRepository.save(ruta);

        rutaRepository.findByNombreStartsWith(ruta.getNombre()+"/").forEach(r->
        {
            r.borrarPropietario(usuario);
            r.borrarMiembro(usuario);
            rutaRepository.save(r);
        });
        

        return "El usuario "+usuario.getUsername()+" fue eliminado como miembro de la ruta.";
    }

    @Override
    public String eliminarRuta(String dir) {

        String separador= System.getProperty("os.name").contains("Windows")?"\\":"/";
        Path directorio=Paths.get(configuracionService.findByParametro("ROOT").getValor()+separador+dir);

        try {
            Files
                .walk(directorio)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
                
            rutaRepository.delete(rutaRepository.findByNombre(dir));
            rutaRepository.findByNombreStartsWith(dir+"/").forEach(r->rutaRepository.delete(r));

            return "Se ha eliminado la ruta "+dir;
        } catch (IOException e) {
            return "Ha ocurrido un error al intentar eliminar la ruta, favor intentarlo más tarde";
        }

    }

    @Override
    public int permisoRutaAnterior(Ruta ruta,Usuario usuario) {
        
        if(ruta.getNivel()==1)
        {
            if(!ruta.getMiembros().contains(usuario))
            {
                ruta.agregarMiembro(usuario);
                rutaRepository.save(ruta);
            }
            return 0;
        }
        else
        {
            if(!ruta.getMiembros().contains(usuario))
            {
                ruta.agregarMiembro(usuario);
                rutaRepository.save(ruta);
            }
            String rutaAnt=rutaAnterior(ruta);
            Ruta ra=rutaRepository.findByNombre(rutaAnt);
            return permisoRutaAnterior(ra,usuario);
        }
    }

    @Override
    public String rutaAnterior(Ruta ruta) {
        String[] parts=ruta.getNombre().split("/");
        String rutaAnterior="";
        if(parts.length==1)
            return rutaAnterior;
        else
        {
            for(int i=0;i<=parts.length-2;i++)
            {
                rutaAnterior+=parts[i]+"/";
            }
            rutaAnterior=rutaAnterior.substring(0, rutaAnterior.length()-1);

            return rutaAnterior;
        }
    }

    @Override
    public Boolean soyPropietario(String ruta,String usuario) {
        Ruta r=rutaRepository.findByNombre(ruta);
        Usuario u=usuarioRepository.findByUsername(usuario);
        return r.getPropietarios().contains(u);
    }
    
}
