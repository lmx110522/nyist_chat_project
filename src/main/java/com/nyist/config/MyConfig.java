package com.nyist.config;


import com.nyist.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInteceptor;


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInteceptor).addPathPatterns("/**").excludePathPatterns("/login","/loginUI").excludePathPatterns("/js/**","/chat/**","/layui/**");
    }
}
