package com.catering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main Spring Boot Application Class for Catering Management System
 * 
 * This application provides comprehensive management tools for catering services including:
 * - Order Management
 * - Resource Management  
 * - Employee Management
 * - Client & Vendor Management
 * - Task Management
 * - Reports & Dashboards
 */
@SpringBootApplication
public class CateringApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CateringApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CateringApplication.class, args);
    }
}

