package com.ghb.springboot.cloud.app.service;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.repository.IUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    private IUsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired    
    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void crearUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios=usuarioRepository.findAll();
        
        return usuarios;
    }

    @Transactional
    @Override
    public void cambiarPassword(String username, String password) {
        Usuario usuario=usuarioRepository.findByUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        
        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void editarUsuario(Long id, String nombre,String password, String apep, String apem, Boolean estado,String rol) {
        Usuario usuario=usuarioRepository.findById(id).orElse(null);

        if(usuario==null)
            throw new UsernameNotFoundException("No se encontró el usuario con ID "+id);
        
        usuario.setNombre(nombre);
        usuario.setApellidoPat(apep);
        usuario.setApellidoMat(apem);
        if(password!="")
            usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEnabled(estado);
        usuario.setRol(rol);

        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario findOne(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    
}
