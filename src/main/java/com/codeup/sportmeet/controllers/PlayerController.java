package com.codeup.sportmeet.controllers;


import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.PlayerRepository;
import com.codeup.sportmeet.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

@Controller
public class PlayerController {

    private PlayerRepository playerDao;
    private PasswordEncoder passwordEncoder;
    private RatingRepository ratingDao;

    public PlayerController(PlayerRepository playerDao, PasswordEncoder passwordEncoder) {
        this.playerDao = playerDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${FILESTACK_API}")
    private String fsKey;

    @GetMapping("/players")
    public String playerIndex(Model model) {
        model.addAttribute("players", playerDao.findAll());
        return "/player/index";
    }

    @GetMapping("/sign-up")
    public String showCreateForm(Model model) {
        model.addAttribute("player", new Player());
        return "/player/sign-up";
    }

    @PostMapping("/sign-up")
    public String playerCreate(@ModelAttribute Player player) {
        String hash = passwordEncoder.encode(player.getPassword());
        player.setProfilePicUrl("https://cdn.filestackcontent.com/lZzcLaMGTMa9KovP6nxh");
        player.setPassword(hash);
        playerDao.save(player);

        return "redirect:/login";
    }

    @GetMapping("/player/{id}")
    public String showPlayer(@PathVariable long id, Model model) {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("player", playerDao.getById(currentPlayer.getId()));
        return ("/player/show");
    }

    @GetMapping("player/{id}/edit")
    public String playerEdit(Model model, @PathVariable long id) {
        model.addAttribute("player", playerDao.getById(id));
        return "/player/edit";
    }

    @GetMapping("player/{id}/delete")
    public String playerDelete(@PathVariable long id) {
        playerDao.deleteById(id);
        return "redirect:/players";
    }

    @GetMapping("player/search/{name}")
    public String findPlayerByName(@PathVariable String name, Model model) {
        model.addAttribute("players", playerDao.searchByPlayerLike(name));
        return "/player/index";
    }

    @GetMapping("/profile/{id}/upload")
    public String viewAddProfilePhoto(@PathVariable long id, Model model, HttpSession session) {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("id", currentPlayer.getId());
        model.addAttribute("fsKey", fsKey);
        model.addAttribute("player", playerDao.getById(id));
        return "player/upload";
    }

    @PostMapping("/profile/{id}/upload")
    public String savePhoto(@RequestParam(name = "profile_img") String img, @PathVariable long id) {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Player player = playerDao.getById(id);
        player.setProfilePicUrl(img);
        playerDao.save(player);
        return "redirect:/player/" + currentPlayer.getId();
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        return "/player/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String submitForgotPassword(@RequestParam(name = "email") String email, @RequestParam(name = "username") String username, Model model) {
        if (playerDao.findByUsernameAndEmail(username, email) != null) {
            Player player = playerDao.findByUsernameAndEmail(username, email);
            model.addAttribute("player", player);
            return "redirect:/set/" + player.getId() + "/password";
        } else {
            return "/player/forgotPassword";
        }
    }

    @GetMapping("/set/{id}/password")
    public String showResetPassword(@PathVariable long id, Model model) {
        model.addAttribute("player", playerDao.getById(id));
        return "/player/set-password";
    }

    @PostMapping("/set/{id}/password")
    public String submitResetPassword(@ModelAttribute Player player, @RequestParam(name = "password") String password, @RequestParam(name = "confirm-password") String confirmPassword) {
        Player changePlayer = playerDao.getById(player.getId());
        if (password.equals(confirmPassword)) {
            String hash = passwordEncoder.encode(password);
            changePlayer.setPassword(hash);
            playerDao.save(changePlayer);
        }
        return "redirect:/login";
    }
}
