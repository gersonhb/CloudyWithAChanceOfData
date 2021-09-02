package com.ghb.springboot.cloud.app.configuracion;

import com.ghb.springboot.cloud.app.repository.IConfiguracionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class ServerConfig{
    
    @Autowired
    private IConfiguracionRepository configuracionRepository;

    @Bean()
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(Long.parseLong(
          configuracionRepository.findByParametro("MAX_FILE_UPLOAD").getValor()) * 1024 * 1024);
        return multipartResolver;
      }
}
