package com.cpu.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Login Controller
 * Serves the login page
 */
@Controller
public class LoginController {

    /**
     * Display login page
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Display registration page (placeholder - can be implemented later)
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}
