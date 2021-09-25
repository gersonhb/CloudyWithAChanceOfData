package com.ghb.springboot.cloud.app.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController{
    
    @GetMapping("/error")
    public String manejoError(HttpServletRequest request)
    {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error_404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error_500";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/error_403";
            }
            else if(statusCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
                return "redirect:/directorio";
            }
            else
                return "error/error";
        }
        return "error/error";
    }

}
