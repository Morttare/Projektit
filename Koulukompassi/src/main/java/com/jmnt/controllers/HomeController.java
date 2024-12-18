package com.jmnt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        List<String> apiEndpoints = Arrays.asList(
                "/api/combined",
                "/api/schools",
                "/api/excel/universities",
                "/api/points",
                "/api/unicoordinates",
                "/api/hscoordinates",
                "/api/wherestudy",
                "/api/wherestudyspecific",
                "/universities"
        );

        model.addAttribute("apiEndpoints", apiEndpoints);
        return "index";
    }
}
