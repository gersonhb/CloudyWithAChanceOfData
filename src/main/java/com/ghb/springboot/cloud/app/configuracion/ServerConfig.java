package com.ghb.springboot.cloud.app.configuracion;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ghb.springboot.cloud.app.repository.IConfiguracionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class ServerConfig implements HttpSessionListener {

  @Autowired
  private IConfiguracionRepository configuracionRepository;

  @Bean
  @DependsOn("initService")
  public CommonsMultipartResolver commonsMultipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

    Long size = configuracionRepository.findByParametro("MAX_FILE_UPLOAD") != null
        ? Long.parseLong(configuracionRepository.findByParametro("MAX_FILE_UPLOAD").getValor())
        : 5;

    multipartResolver.setMaxUploadSize(size * 1024 * 1024);

    return multipartResolver;
  }

  @Override
  public void sessionCreated(HttpSessionEvent se) {

    Integer time = configuracionRepository.findByParametro("SESSION_TIMEOUT") != null
        ? Integer.parseInt(configuracionRepository.findByParametro("SESSION_TIMEOUT").getValor())
        : 15;

    se.getSession().setMaxInactiveInterval(time * 60);
  }

  @Bean
  public TomcatServletWebServerFactory webServerFactory() {
    Integer port = configuracionRepository.findByParametro("PORT_SERVER") != null
        ? Integer.parseInt(configuracionRepository.findByParametro("PORT_SERVER").getValor())
        : 8080;

    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.setPort(port);

    return factory;
  }
}
