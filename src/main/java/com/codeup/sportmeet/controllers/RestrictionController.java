package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.repositories.RestrictionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestrictionController {

    private final RestrictionRepository restrictionDao;

    public RestrictionController(RestrictionRepository restrictionDao) {
        this.restrictionDao = restrictionDao;
    }

    @GetMapping("/restrictions")
    public String showRestrictions(Model model){
         model.addAttribute("restrictions", restrictionDao.findAll());
         return "/restriction/index";
    }
}
