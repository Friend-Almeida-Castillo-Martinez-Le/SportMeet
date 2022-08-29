package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.repositories.RankingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

    private RankingRepository rankingDao;

    public RankingController(RankingRepository rankingDao) {
        this.rankingDao = rankingDao;
    }

    @GetMapping("/rankings")
    public String showRankings(Model model){
        model.addAttribute("rankings", rankingDao.findAll());
        return "/ranking/index";
    }
}
