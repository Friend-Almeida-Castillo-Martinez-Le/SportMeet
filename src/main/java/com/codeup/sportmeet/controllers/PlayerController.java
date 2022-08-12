package com.codeup.sportmeet.controllers;


import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.PlayerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String showCreateForm(Model model) {
        model.addAttribute("player", new Player());
        return "/player/create";
    }

    @PostMapping("player/create")
    public String playerCreate(@ModelAttribute Player player) {
        playerDao.save(player);
        return "redirect:/players";
    }

    @GetMapping("/player/{id}/show")
    public String showPlayer(@PathVariable long id, Model model){
        model.addAttribute("player", playerDao.getById(id));
        return ("/player/show");
    }

    @GetMapping("player/{id}/edit")
    public String playerEdit(Model model, @PathVariable long id){
        model.addAttribute("player", playerDao.getById(id));
        return "/player/edit";
    }

    @GetMapping("player/{id}/delete")
    public String playerDelete(@PathVariable long id){
        playerDao.deleteById(id);
        return "redirect:/players";
    }




}
