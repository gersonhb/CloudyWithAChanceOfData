package com.ghb.springboot.cloud.app.service;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public interface ICifradoService {

    public SecretKey generarClave();
    public IvParameterSpec generarIv();
    public String guardarKey();
    public String guardarIv();
    public IvParameterSpec leerIv();
    public SecretKey leerKey();
    public void encriptar(String ruta);
    public void desencriptar(String ruta);
    
}
