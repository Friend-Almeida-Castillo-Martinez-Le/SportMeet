package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Rating;
import com.codeup.sportmeet.repositories.RatingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/rating/show/{id}")
    public String showRating(@PathVariable long id, Model model){
        model.addAttribute("ratings", ratingDao.searchRatingForRatee(id));
        return "/rating/show";


    }
}
