package com.ghb.springboot.cloud.app.service;

import javax.servlet.http.HttpServletResponse;

public interface IDatabaseTools{
    
    public void backupDb(HttpServletResponse response);
    public void restoresDb();
    public Long sizeDatabase();
    public String nameDatabase();
}
