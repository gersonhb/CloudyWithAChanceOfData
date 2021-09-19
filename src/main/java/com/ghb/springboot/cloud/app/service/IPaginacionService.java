package com.ghb.springboot.cloud.app.service;

import com.ghb.springboot.cloud.app.entity.Ruta;
import com.ghb.springboot.cloud.app.entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPaginacionService {

    Page<Usuario> getAllUsuarios(Pageable pageable);
    Page<Ruta> getPropietariosRuta(Long idUsuario,Pageable pageable);
    Page<Ruta> getAllRutas(Pageable pageable);
    Page<Usuario> getPropietariosUsuario(String nomRuta,Pageable pageable);
    Page<Usuario> getMiembrosUsuario(String nomRuta,Pageable pageable);
}
