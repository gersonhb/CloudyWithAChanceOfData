package com.ghb.springboot.cloud.app.service;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Usuario;

public interface IUsuarioService {
    
    public void crearUsuario(Usuario usuario);
    public List<Usuario> listarUsuarios();
    public void cambiarPassword(String username, String pass);
    public void editarUsuario(Long id,String password, Boolean estado,String rol);
    public Usuario findOne(Long id);
    public Usuario findByUsername(String username);
    public void initUsuario();
    public Boolean nombreUsuario(String username);
}