package com.snippetgen.codesnippetgen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // Ensure this handles the root URL
    public String home() {
        return "index"; // Ensure index.html exists in src/main/resources/templates
    }
}
