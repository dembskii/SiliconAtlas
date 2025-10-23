package com.example.HelloGradle3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("msg", "Hello Spring Boot 3 + Thymeleaf + Java 17");
        return "index"; // szuka templates/index.html
    }
}