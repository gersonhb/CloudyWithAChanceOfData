package com.ghb.springboot.cloud.app.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Archivo {

    private String nombre;
    private Long tamano;
    private LocalDateTime fecMod;
    private Boolean esDirectorio;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTamano() {
        return tamano;
    }

    public void setTamano(Long tamano) {
        this.tamano = tamano;
    }

    public LocalDateTime getFecMod() {
        return fecMod;
    }

    public void setFecMod(LocalDateTime fecMod) {
        this.fecMod = fecMod;
    }

    public Boolean getEsDirectorio() {
        return esDirectorio;
    }

    public void setEsDirectorio(Boolean esDirectorio) {
        this.esDirectorio = esDirectorio;
    }

    public String getSize()
    {
        String space="";

        if(getTamano()>=1024 && getTamano()<1048576)
            space=(getTamano()/1024)+" KB";
        else if(getTamano()>=1048576 && getTamano()<1073741824)
            space=(getTamano()/1048576)+" MB";
        else if(getTamano()>=1073741824)
            space=(getTamano()/1073741824)+" GB";
        else
            space=getTamano()+" B";

        return space;
    }

    private String getFormatoFecha()
    {
        return getFecMod().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Archivo(String nombre, Long tamano, LocalDateTime fecMod, Boolean esDirectorio) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.fecMod = fecMod;
        this.esDirectorio = esDirectorio;
    }

    @Override
    public String toString() {
        return "File [esDirectorio=" + esDirectorio + ", fecMod=" + fecMod + ", nombre=" + nombre + ", tamano=" + tamano
                + "]";
    }

}
