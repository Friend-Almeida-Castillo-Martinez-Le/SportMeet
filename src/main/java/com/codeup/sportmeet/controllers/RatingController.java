package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Rating;
import com.codeup.sportmeet.repositories.PlayerRepository;
import com.codeup.sportmeet.repositories.RatingRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RatingController {

    private RatingRepository ratingDao;
    private PlayerRepository playerDao;

    public RatingController(RatingRepository ratingDao, PlayerRepository playerDao) {
        this.playerDao = playerDao;
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

    @PostMapping("/rating/{id}/edit")
    public String editRating(@PathVariable long id,Model model, @RequestParam(name = "rating") long rating){
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Rating r = ratingDao.getById(id);
        r.setRating(rating);
        r.setRated(true);
        ratingDao.save(r);
        model.addAttribute("player", playerDao.getById(currentPlayer.getId()));
        model.addAttribute("allratings", ratingDao.searchRatingForRatee(currentPlayer.getId()));
        model.addAttribute("teamratings", ratingDao.searchRatingForRater(currentPlayer.getId()));
        return "/player/show";
    }

}
