package com.ghb.springboot.cloud.app.service;

import org.springframework.http.ResponseEntity;

public interface IDatabaseTools{
    
    public ResponseEntity<Object> backupDb();
    public void restoresDb();
    public Long sizeDatabase();
    public String nameDatabase();
}
