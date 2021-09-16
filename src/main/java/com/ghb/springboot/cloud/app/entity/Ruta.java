package com.ghb.springboot.cloud.app.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "rutas")
public class Ruta implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruta_id")
    private Long id;

    @NotBlank
    @Column(nullable = false,unique = true)
    private String nombre;

    private Integer nivel;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ruta_usuario_propietario",
    joinColumns = @JoinColumn(name = "ruta_id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "usuario_id"))
    private List<Usuario> propietarios;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ruta_usuario_miembro",
    joinColumns = @JoinColumn(name = "ruta_id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "usuario_id"))
    private List<Usuario> miembros;
    
    @PrePersist
    public void prePersist()
    {
        this.nivel=this.nombre.split("/").length-1;
    }

    public Ruta() {
    }

    public Ruta(@NotBlank String nombre, List<Usuario> propietarios, List<Usuario> miembros) {
        this.nombre = nombre;
        this.propietarios = propietarios;
        this.miembros = miembros;
    }

    public Ruta(@NotBlank String nombre, Integer nivel, List<Usuario> propietarios, List<Usuario> miembros) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.propietarios = propietarios;
        this.miembros = miembros;
    }

    public Ruta(Long id, @NotBlank String nombre, Integer nivel, List<Usuario> propietarios, List<Usuario> miembros) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.propietarios = propietarios;
        this.miembros = miembros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public List<Usuario> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<Usuario> propietarios) {
        this.propietarios = propietarios;
    }

    public List<Usuario> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Usuario> miembros) {
        this.miembros = miembros;
    }

    public void agregarPropietario(Usuario propietario)
    {
        this.propietarios.add(propietario);
    }

    public void agregarMiembro(Usuario miembro)
    {
        this.miembros.add(miembro);
    }

    public void borrarPropietario(Usuario propietario)
    {
        this.propietarios.remove(propietario);
    }

    public void borrarMiembro(Usuario miembro)
    {
        this.miembros.remove(miembro);
    }

}
