package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Rating;
import com.codeup.sportmeet.repositories.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RatingController {

    private RatingRepository ratingDao;

    public RatingController(RatingRepository ratingDao) {
        this.ratingDao = ratingDao;
    }

    @GetMapping("/ratings")
    public String showRatingIndex(Model model){
        model.addAttribute("ratings", ratingDao.findAll());
        return "rating/index";
    }

    @GetMapping("/rating/create")
    public String createRating(Model model){
        model.addAttribute("rating", new Rating());
        return "rating/create";
    }

    @PostMapping("/rating/create")
    public String createRatingPart2(@ModelAttribute Rating rating, Model model){
        ratingDao.save(rating);
        return "redirect:/ratings";
    }
}
