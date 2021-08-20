package com.ghb.springboot.cloud.app.configuracion;

import com.ghb.springboot.cloud.app.entity.Rol;
import com.ghb.springboot.cloud.app.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/css/**","/js/**","/images/**","/h2-console/**").permitAll()
        .antMatchers("/admin/**").hasAnyRole(Rol.ADMINISTRADOR.name())
        .anyRequest()
        .authenticated()
        .and()
        .formLogin().loginPage("/login").permitAll()
        .defaultSuccessUrl("/")
        .and()
        .logout()
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("/login")
        .and()
        .headers().frameOptions().disable();
    }

}