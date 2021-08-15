package com.ghb.springboot.cloud.app.repository;

import com.ghb.springboot.cloud.app.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario,Long>{

    public Usuario findByUsername(String username);
    
}
