package com.codeup.sportmeet.controllers;


import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.PlayerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlayerController {

    private PlayerRepository playerDao;

    public PlayerController(PlayerRepository playerDao) {
        this.playerDao = playerDao;
    }

    @GetMapping("/players")
    public String playerIndex(Model model){
        model.addAttribute("players", playerDao.findAll());
        return "/player/index";
    }

    @GetMapping("player/create")
    public String playerCreate(Model model) {
        playerDao.save(new Player("player1", "pass", "emailmeplayer", "Leeroy", "Jenkins"));
        return "redirect:/players";
    }

    @GetMapping("player/{id}/edit")
    public String playerEdit(Model model, @PathVariable long id){
        model.addAttribute("player", playerDao.getById(id));
        return "/player/show";
    }

}
