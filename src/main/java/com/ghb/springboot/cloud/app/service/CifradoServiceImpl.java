package com.ghb.springboot.cloud.app.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ghb.springboot.cloud.app.entity.Configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CifradoServiceImpl implements ICifradoService {

    @Autowired
    private IConfiguracionService configuracionService;

    @Override
    public SecretKey generarClave() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey clave = keyGenerator.generateKey();
            return clave;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    @Override
    public IvParameterSpec generarIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @Override
    public String guardarKey() {

        Configuracion configuracion = configuracionService.findByParametro("KEY_RUTA");
        String mensaje = "";
        String key="";
        if(System.getProperty("os.name").contains("Windows"))
            key="\\key";
        else
            key="/key";

        if (configuracion.getValor() == "") {
            try {
                FileOutputStream outputStreamKey = new FileOutputStream(System.getProperty("user.dir") + key);

                outputStreamKey.write(generarClave().getEncoded());

                outputStreamKey.close();

                configuracionService.updateConfiguracion("KEY_RUTA", System.getProperty("user.dir") + key);
                configuracionService.updateConfiguracion("KEY_FILE", "1");

                mensaje = "Se generó KEY exitosamente";

            } catch (Exception e) {
                mensaje="Error al intentar generar KEY, podrías no tener permiso de escritura en la ruta del aplicativo";

            }
        } else
            mensaje = "Ya existe un KEY generado, si desea cambie la ruta del archivo pero podría perder los archivos cifrados con el KEY previo";

        return mensaje;
    }

    @Override
    public String guardarIv() {

        Configuracion configuracion = configuracionService.findByParametro("IV_RUTA");
        String mensaje = "";
        String iv="";
        if(System.getProperty("os.name").contains("Windows"))
            iv="\\iv";
        else
            iv="/iv";

        if (configuracion.getValor() == "") {
            try {
                FileOutputStream outputStreamIv = new FileOutputStream(System.getProperty("user.dir") + iv);

                outputStreamIv.write(generarIv().getIV());

                outputStreamIv.close();

                configuracionService.updateConfiguracion("IV_RUTA", System.getProperty("user.dir") + iv);
                configuracionService.updateConfiguracion("IV_FILE", "1");

                mensaje = "Se generó IV exitosamente";

            } catch (Exception e) {
                mensaje="Error al intentar generar IV, podrías no tener permiso de escritura en la ruta del aplicativo";
            }
        } else
            mensaje = "Ya existe un IV generado, si desea cambie la ruta del archivo pero podría perder los archivos cifrados con el IV previo";

        return mensaje;
    }

    @Override
    public IvParameterSpec leerIv() {
        try {
            FileInputStream inputStreamIv = new FileInputStream(
                    configuracionService.findByParametro("IV_RUTA").getValor());
            byte[] iv = inputStreamIv.readAllBytes();
            inputStreamIv.close();
            return new IvParameterSpec(iv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SecretKey leerKey() {
        try {
            FileInputStream inputStreamKey = new FileInputStream(
                    configuracionService.findByParametro("KEY_RUTA").getValor());
            byte[] key = inputStreamKey.readAllBytes();
            inputStreamKey.close();
            SecretKey clave = new SecretKeySpec(key, 0, key.length, "AES");
            return clave;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void encriptar(String ruta) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, leerKey(), leerIv());

            FileInputStream inputStream = new FileInputStream(ruta);
            FileOutputStream outputStream = new FileOutputStream(ruta+".lock");
            byte[] buffer = new byte[64];
            int bytesLeer;
            while ((bytesLeer = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesLeer);
                if (output != null) {
                    outputStream.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();
            Files.move(Paths.get(ruta+".lock"), Paths.get(ruta),StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void desencriptar(String ruta) {        
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, leerKey(), leerIv());

            FileInputStream inputStream = new FileInputStream(ruta);
            FileOutputStream outputStream = new FileOutputStream(ruta+".unlock");
            byte[] buffer = new byte[64];
            int bytesLeer;
            while ((bytesLeer = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesLeer);
                if (output != null) {
                    outputStream.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
            
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
