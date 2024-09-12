package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    
    @GetMapping("/")
    @ResponseBody
    public String helloWorld() {
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/about-us")
    @ResponseBody
    public String aboutUss() {
        return "<h1>About Us</h1>";
    }
}
