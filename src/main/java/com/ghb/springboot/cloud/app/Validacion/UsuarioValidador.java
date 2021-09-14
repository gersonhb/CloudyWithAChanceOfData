package com.ghb.springboot.cloud.app.Validacion;

import com.ghb.springboot.cloud.app.entity.Usuario;
import com.ghb.springboot.cloud.app.service.IUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UsuarioValidador implements Validator{

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Usuario.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Usuario usuario=(Usuario)target;
        
        if(usuarioService.nombreUsuario(usuario.getUsername()))
        {
            errors.rejectValue("username", "UsernameUsuario.usuario.username");
        }
        
    }
    
}
