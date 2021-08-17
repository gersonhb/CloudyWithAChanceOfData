package com.ghb.springboot.cloud.app.service;

import java.util.List;

import com.ghb.springboot.cloud.app.entity.Configuracion;
import com.ghb.springboot.cloud.app.repository.IConfiguracionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionServiceImpl implements IConfiguracionService{

    @Autowired
    private IConfiguracionRepository configuracionRepository;

    @Transactional
    @Override
    public void initConfiguracion() {
        Configuracion configuracion=configuracionRepository.findById(1L).orElse(null);

        if(configuracion==null)
        {
            configuracionRepository.saveAll(List.of(
                new Configuracion("ROOT",""),
                new Configuracion("KEY",""),
                new Configuracion("IV","")
            ));
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<Configuracion> findAll() {
        return configuracionRepository.findAll();
    }
    
}
