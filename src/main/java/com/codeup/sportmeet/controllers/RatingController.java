package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.repositories.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RatingController {

    private RatingRepository ratingDao;

    public RatingController(RatingRepository ratingDao) {
        this.ratingDao = ratingDao;
    }

    @GetMapping("/ratings")
    public String showRatingIndex(Model model){
        model.addAttribute("ratings", ratingDao.findAll());
        return "/rating/index";
    }
}
