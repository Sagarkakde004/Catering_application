package com.catering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller for handling page navigation
 */
@Controller
public class WebController {
    
    /**
     * Root path - redirect to dashboard
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    
    /**
     * Dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/index.html";
    }
    
    /**
     * Orders page
     */
    @GetMapping("/orders")
    public String orders() {
        return "forward:/orders.html";
    }
    
    /**
     * Employees page
     */
    @GetMapping("/employees")
    public String employees() {
        return "forward:/employees.html";
    }
    
    /**
     * Resources page
     */
    @GetMapping("/resources")
    public String resources() {
        return "forward:/resources.html";
    }
    
    /**
     * Tasks page
     */
    @GetMapping("/tasks")
    public String tasks() {
        return "forward:/tasks.html";
    }
    
    /**
     * Clients page
     */
    @GetMapping("/clients")
    public String clients() {
        return "forward:/clients.html";
    }
    
    /**
     * Reports page
     */
    @GetMapping("/reports")
    public String reports() {
        return "forward:/reports.html";
    }
}

