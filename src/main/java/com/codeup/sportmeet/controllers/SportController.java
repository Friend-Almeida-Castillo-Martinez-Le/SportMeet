package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SportController {

    private final SportRepository sportDao;

    public SportController(SportRepository sportDao) {
        this.sportDao = sportDao;
    }

    @GetMapping("/sports")
    public String displaySports(Model model){
        model.addAttribute("sports", sportDao.findAll());
        return "/sport/index";
    }
}
