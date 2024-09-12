package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    // @ResponseBody
    // public String helloWorld() {
    //     return "<h1>Hello World</h1>";
    // }
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/about-us")
    //@ResponseBody -> remove to use template
    // public String aboutUs() {
    //     return "<h1>About Us</h1>";
    // }
    // this uses the template file called about-us
    public String aboutUs(Model model) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        model.addAttribute("currentDateTime", currentDateTime);

        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contactUs() {
        return "contact-us";
    }
}
