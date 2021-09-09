package com.ghb.springboot.cloud.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "apellido_paterno",nullable = false)
    private String apellidoPat;

    @NotBlank
    @Column(name = "apellido_materno",nullable = false)
    private String apellidoMat;

    @NotBlank
    @Column(nullable = false,unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String rol;

    private Boolean enabled;
    private LocalDate fechaCreacion;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "propietarios")
    private List<Ruta> propietarios;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "miembros")
    private List<Ruta> miembros;

    @PrePersist
    public void prePersist()
    {
        this.rol="ROLE_"+rol;
        this.fechaCreacion=LocalDate.now();
        this.nombre=this.nombre.toUpperCase();
        this.apellidoPat=this.apellidoPat.toUpperCase();
        this.apellidoMat=this.apellidoMat.toUpperCase();
        this.username=this.username.toLowerCase();
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
    public String getApellidoPat() {
        return apellidoPat;
    }
    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }
    public String getApellidoMat() {
        return apellidoMat;
    }
    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Ruta> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<Ruta> propietarios) {
        this.propietarios = propietarios;
    }

    public List<Ruta> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Ruta> miembros) {
        this.miembros = miembros;
    }

    public Usuario() {
    }

    public Usuario(@NotBlank String nombre, @NotBlank String apellidoPat, @NotBlank String apellidoMat,
            @NotBlank String username, @NotBlank String password, @NotBlank String rol, Boolean enabled,
            LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.enabled = enabled;
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario(Long id, @NotBlank String nombre, @NotBlank String apellidoPat, @NotBlank String apellidoMat,
            @NotBlank String username, @NotBlank String password, @NotBlank String rol, Boolean enabled,
            LocalDate fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.enabled = enabled;
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario(Long id, @NotBlank String nombre, @NotBlank String apellidoPat, @NotBlank String apellidoMat,
            @NotBlank String username, @NotBlank String password, @NotBlank String rol, Boolean enabled,
            LocalDate fechaCreacion, List<Ruta> propietarios, List<Ruta> miembros) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.enabled = enabled;
        this.fechaCreacion = fechaCreacion;
        this.propietarios = propietarios;
        this.miembros = miembros;
    }

    @Override
    public String toString() {
        return "Usuario [apellidoMat=" + apellidoMat + ", apellidoPat=" + apellidoPat + ", enabled=" + enabled
                + ", fechaCreacion=" + fechaCreacion + ", id=" + id + ", miembros=" + miembros + ", nombre=" + nombre
                + ", password=" + password + ", propietarios=" + propietarios + ", rol=" + rol + ", username="
                + username + "]";
    }

}
