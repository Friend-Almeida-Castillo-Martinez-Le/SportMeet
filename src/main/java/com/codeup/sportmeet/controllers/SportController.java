package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/sports/create/{name}")
    public String createSport(@PathVariable String name, Model model){
        sportDao.save(new Sport(name));
        return "redirect:/sports";
    }
}
