package com.ghb.springboot.cloud.app.service;

import com.ghb.springboot.cloud.app.entity.Ruta;
import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.repository.IRutaRepository;
import com.ghb.springboot.cloud.app.repository.IUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaginacionServiceImpl implements IPaginacionService{

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IRutaRepository rutaRepository;
    
    @Override
    public Page<Usuario> getAllUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public Page<Ruta> getPropietariosRuta(Long idUsuario,Pageable pageable) {
        return rutaRepository.findPropietariosRuta(idUsuario, pageable);
    }

    @Override
    public Page<Usuario> getPropietariosUsuario(String nomRuta, Pageable pageable) {
        return usuarioRepository.findPropietariosUsuario(nomRuta, pageable);
    }

    @Override
    public Page<Usuario> getMiembrosUsuario(String nomRuta, Pageable pageable) {
        return usuarioRepository.findMiembrosUsuario(nomRuta, pageable);
    }
    
    
}
