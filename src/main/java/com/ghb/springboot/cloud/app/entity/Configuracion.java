package com.ghb.springboot.cloud.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuraciones")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parametro;
    private String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Configuracion() {
    }

    public Configuracion(String parametro, String valor) {
        this.parametro = parametro;
        this.valor = valor;
    }

    public Configuracion(Long id, String parametro, String valor) {
        this.id = id;
        this.parametro = parametro;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Configuracion [id=" + id + ", parametro=" + parametro + ", valor=" + valor + "]";
    }

}
