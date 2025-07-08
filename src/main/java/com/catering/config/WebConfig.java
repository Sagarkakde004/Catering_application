package com.catering.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration for static resources and view controllers
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Static resources (CSS, JS, images)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "file:src/main/webapp/static/");
        
        // HTML pages
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", 
                                    "file:src/main/webapp/", "classpath:/META-INF/resources/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Default page
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/dashboard").setViewName("forward:/index.html");
    }
}

