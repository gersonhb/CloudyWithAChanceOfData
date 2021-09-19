package com.ghb.springboot.cloud.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][ a-zA-Z]{5,20}")
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][ a-zA-Z]{5,20}")
    @Column(name = "apellido_paterno",nullable = false)
    private String apellidoPat;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][ a-zA-Z]{5,20}")
    @Column(name = "apellido_materno",nullable = false)
    private String apellidoMat;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z\\d]{5,15}")
    @Column(nullable = false,unique = true)
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$")
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

    public String fechaFormato()
    {
        return this.fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apellidoMat == null) ? 0 : apellidoMat.hashCode());
        result = prime * result + ((apellidoPat == null) ? 0 : apellidoPat.hashCode());
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((miembros == null) ? 0 : miembros.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((propietarios == null) ? 0 : propietarios.hashCode());
        result = prime * result + ((rol == null) ? 0 : rol.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (apellidoMat == null) {
            if (other.apellidoMat != null)
                return false;
        } else if (!apellidoMat.equals(other.apellidoMat))
            return false;
        if (apellidoPat == null) {
            if (other.apellidoPat != null)
                return false;
        } else if (!apellidoPat.equals(other.apellidoPat))
            return false;
        if (enabled == null) {
            if (other.enabled != null)
                return false;
        } else if (!enabled.equals(other.enabled))
            return false;
        if (fechaCreacion == null) {
            if (other.fechaCreacion != null)
                return false;
        } else if (!fechaCreacion.equals(other.fechaCreacion))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (miembros == null) {
            if (other.miembros != null)
                return false;
        } else if (!miembros.equals(other.miembros))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (propietarios == null) {
            if (other.propietarios != null)
                return false;
        } else if (!propietarios.equals(other.propietarios))
            return false;
        if (rol == null) {
            if (other.rol != null)
                return false;
        } else if (!rol.equals(other.rol))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    

}
