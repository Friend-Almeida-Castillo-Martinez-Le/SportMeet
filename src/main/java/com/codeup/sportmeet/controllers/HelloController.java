package com.codeup.sportmeet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class HelloController {

    @GetMapping("/")
    public String mainWelcome(Model model) {
        model.addAttribute("message", "Main Landing page.");
        return "/event/create";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "welcome message test");
        return "/hello";
    }

    @GetMapping("/register")
    public String registerPlayer(){
        return "/register";
    }

    @GetMapping("/signin")
    public String signIn(){
        return "/signin";
    }
}