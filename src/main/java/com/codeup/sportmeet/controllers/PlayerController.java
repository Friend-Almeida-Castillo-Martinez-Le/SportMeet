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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return "player/index";
    }

    @GetMapping("/sign-up")
    public String showCreateForm(Model model) {
        model.addAttribute("fsKey", fsKey);
        model.addAttribute("player", new Player());
        return "player/sign-up";
    }

    @PostMapping("/sign-up")
    public String playerCreate(@ModelAttribute Player player, @RequestParam("profile_img") String url, @RequestParam("password")String password1, @RequestParam("confirm-password") String confirmPassword) {
        String password = player.getPassword();
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return "redirect:/sign-up";
        } else {
            if (password1.equals(confirmPassword)) {
                String hash = passwordEncoder.encode(player.getPassword());
                if (!url.equals("")) {
                    player.setProfilePicUrl(url);
                } else {
                    player.setProfilePicUrl("https://cdn.filestackcontent.com/lZzcLaMGTMa9KovP6nxh");
                }
                player.setPassword(hash);
                if (playerDao.findByUsernameAndEmail(player.getUsername(), player.getEmail()) != null) {
                    return "redirect:/sign-up";
                } else {
                    playerDao.save(player);
                    return "redirect:/login";
                }
            }
            else {
                return "redirect:/sign-up";
            }
        }
    }

    @GetMapping("/player/{id}")
    public String showPlayer(@PathVariable long id, Model model) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("player", playerDao.getById(currentPlayer.getId()));
            return ("player/show");
        }
    }

    @GetMapping("player/{id}/edit")
    public String playerEdit(Model model, @PathVariable long id) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            model.addAttribute("player", playerDao.getById(id));
            return "player/edit";
        }
    }

    @PostMapping("player/{id}/edit")
    public String submitPlayerEdit(@ModelAttribute Player player, @RequestParam(name = "password") String password, @RequestParam(name = "confirm-password") String confirmPassword) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            if (player.getUsername() != null) {
                if (playerDao.findByUsernameAndEmail(player.getUsername(), player.getEmail()) != null) {
                    return "redirect:/player/" + player.getId() + "/edit";
                } else  {
                    playerDao.updatePlayerUsername(player.getId(), player.getUsername());
                }
            }
            if (password != null && confirmPassword != null && password.equals(confirmPassword)) {
                String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()])(?=\\S+$).{8,20}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(password);
                if (!matcher.matches()) {
                    return "redirect:/player/" + player.getId() + "/edit";
                } else {
                    String hash = passwordEncoder.encode(password);
                    playerDao.updatePlayerPassword(player.getId(), hash);
                }
            }
            if (player.getEmail() != null) {
                playerDao.updatePlayerEmail(player.getId(), player.getEmail());
            }
            if (player.getFirstName() != null) {
                playerDao.updatePlayerFirstName(player.getId(), player.getFirstName());
            }
            if (player.getLastName() != null) {
                playerDao.updatePlayerLastName(player.getId(), player.getLastName());
            }
            return "redirect:/player/" + player.getId();
        }
    }

    @GetMapping("player/{id}/delete")
    public String playerDelete(@PathVariable long id) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            playerDao.deleteById(id);
            return "redirect:/players";
        }
    }

    @GetMapping("player/search/{name}")
    public String findPlayerByName(@PathVariable String name, Model model) {
        model.addAttribute("players", playerDao.searchByPlayerLike(name));
        return "player/index";
    }

    @GetMapping("/profile/{id}/upload")
    public String viewAddProfilePhoto(@PathVariable long id, Model model, HttpSession session) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            session.setAttribute("id", currentPlayer.getId());
            model.addAttribute("fsKey", fsKey);
            model.addAttribute("player", playerDao.getById(id));
            return "player/upload";
        }
    }

    @PostMapping("/profile/{id}/upload")
    public String savePhoto(@RequestParam(name = "profile_img") String img, @PathVariable long id) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Player player = playerDao.getById(id);
            player.setProfilePicUrl(img);
            playerDao.save(player);
            return "redirect:/player/" + currentPlayer.getId();
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        return "player/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String submitForgotPassword(@RequestParam(name = "email") String email, @RequestParam(name = "username") String username, Model model) {
        if (playerDao.findByUsernameAndEmail(username, email) != null) {
            Player player = playerDao.findByUsernameAndEmail(username, email);
            model.addAttribute("player", player);
            return "redirect:/set/" + player.getId() + "/password";
        } else {
            return "player/forgotPassword";
        }
    }

    @GetMapping("/set/{id}/password")
    public String showResetPassword(@PathVariable long id, Model model) {
        model.addAttribute("player", playerDao.getById(id));
        return "player/set-password";
    }

    @PostMapping("/set/{id}/password")
    public String submitResetPassword(@ModelAttribute Player player, @RequestParam(name = "password") String password, @RequestParam(name = "confirm-password") String confirmPassword) {
        Player changePlayer = playerDao.getById(player.getId());
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return "redirect:/set/" + changePlayer.getId() + "/password";
        }
        else {
            if (password.equals(confirmPassword)) {
                String hash = passwordEncoder.encode(password);
                changePlayer.setPassword(hash);
                playerDao.save(changePlayer);
                return "redirect:/login";
            } else {
                return "redirect:/set/" + changePlayer.getId() + "/password";
            }
        }
    }
}
