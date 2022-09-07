package com.codeup.sportmeet.controllers;


import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Rating;
import com.codeup.sportmeet.repositories.PlayerRepository;
import com.codeup.sportmeet.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PlayerController {

    private PlayerRepository playerDao;
    private PasswordEncoder passwordEncoder;
    private RatingRepository ratingDao;

    public PlayerController(PlayerRepository playerDao, PasswordEncoder passwordEncoder, RatingRepository ratingDao) {
        this.playerDao = playerDao;
        this.passwordEncoder = passwordEncoder;
        this.ratingDao = ratingDao;
    }

    @Value("${FILESTACK_API}")
    private String fsKey;

    @GetMapping("/players")
    public String playerIndex(Model model) {
        model.addAttribute("players", playerDao.findAll());
        return "player/index";
    }

    @GetMapping("/sign-up")
    public String showCreateForm(Model model, @RequestParam(value = "usernameError", required = false) boolean usernameError, @RequestParam(value = "passwordError", required = false) boolean passwordError, @RequestParam(value = "confirmError", required = false) boolean confirmError, @RequestParam(value = "emailError", required = false) boolean emailError) {
        if (usernameError) {
            model.addAttribute("usernameError", usernameError);
        }
        if (passwordError) {
            model.addAttribute("passwordError", passwordError);
        }
        if (confirmError) {
            model.addAttribute("confirmError", confirmError);
        }
        if (emailError) {
            model.addAttribute("emailError", emailError);
        }
        model.addAttribute("fsKey", fsKey);
        model.addAttribute("player", new Player());
        return "player/sign-up";
    }

    @PostMapping("/sign-up")
    public String playerCreate(@ModelAttribute Player player, @RequestParam("profile_img") String url, @RequestParam("password")String password1, @RequestParam("confirm-password") String confirmPassword, Model model) {
        String password = player.getPassword();
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (playerDao.findByUsername(player.getUsername()) != null) {
            return "redirect:/sign-up?usernameError=true";
        }
        if (playerDao.findByEmail(player.getEmail()) != null) {
            return "redirect:/sign-up?emailError=true";
        }
        if (!matcher.matches()) {
            return "redirect:/sign-up?passwordError=true";
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
                    return "redirect:/sign-up?usernameError=true";
                } else {
                    playerDao.save(player);
                    return "redirect:/login";
                }
            }
            else {
                model.addAttribute("password-error", "* Passwords Did Not Match.");
                return "redirect:/sign-up?confirmError=true";
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
            model.addAttribute("allratings", ratingDao.searchRatingForRatee(currentPlayer.getId()));
            model.addAttribute("teamratings", ratingDao.searchRatingForRater(currentPlayer.getId()));

            return ("player/show");
        }
    }

    @GetMapping("/player/{id}/show")
    public String showPlayerShow(@PathVariable long id, Model model) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Player currentPlayer = playerDao.getById(id);
            model.addAttribute("player", playerDao.getById(currentPlayer.getId()));
            model.addAttribute("allratings", ratingDao.searchRatingForRatee(currentPlayer.getId()));
            model.addAttribute("teamratings", ratingDao.searchRatingForRater(currentPlayer.getId()));

            return ("player/show");
        }
    }

    @GetMapping("player/{id}/edit")
    public String playerEdit(Model model, @PathVariable long id, @RequestParam(value = "usernameError", required = false) boolean usernameError, @RequestParam(value = "passwordError", required = false) boolean passwordError, @RequestParam(value = "emailError", required = false) boolean emailError) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            model.addAttribute("player", playerDao.getById(id));
            model.addAttribute("fsKey", fsKey);
            model.addAttribute("passwordError", passwordError);
            model.addAttribute("usernameError", usernameError);
            model.addAttribute("emailError", emailError);
            return "player/edit";
        }
    }

    @PostMapping("player/{id}/edit")
    public String submitPlayerEdit(@ModelAttribute("player") Player player, @RequestParam(name = "password") String password, @RequestParam(name = "confirm-password") String confirmPassword, @RequestParam("profile_img") String url) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (player.getUsername() != null) {
                if (playerDao.findByUsername(player.getUsername()) != null && !player.getUsername().equalsIgnoreCase(playerDao.getById(currentPlayer.getId()).getUsername())) {
                    return "redirect:/player/" + player.getId() + "/edit?usernameError=true";
                } else  {
                    playerDao.updatePlayerUsername(player.getId(), player.getUsername());
                }
            }
            if (!password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword)) {
                String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()])(?=\\S+$).{8,20}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(password);
                if (!matcher.matches()) {
                    return "redirect:/player/" + player.getId() + "/edit?passwordError=true";
                } else {
                    String hash = passwordEncoder.encode(password);
                    playerDao.updatePlayerPassword(player.getId(), hash);
                }
            }
            if (player.getEmail() != null) {
                if (playerDao.findByEmail(player.getEmail()) != null && !player.getEmail().equalsIgnoreCase(playerDao.getById(currentPlayer.getId()).getEmail())) {
                    return "redirect:/player/" + player.getId() + "/edit?emailError=true";
                } else {
                    playerDao.updatePlayerEmail(player.getId(), player.getEmail());
                }
            }
            if (player.getFirstName() != null) {
                playerDao.updatePlayerFirstName(player.getId(), player.getFirstName());
            }
            if (player.getLastName() != null) {
                playerDao.updatePlayerLastName(player.getId(), player.getLastName());
            }
            if (!url.equals("")) {
                playerDao.updatePlayerProfilePic(player.getId(), url);
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
    public String forgotPasswordForm(Model model, @RequestParam(value = "usernameError", required = false) boolean usernameError) {
        if (usernameError) {
            model.addAttribute("usernameError", usernameError);
        }
        return "player/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String submitForgotPassword(@RequestParam(name = "email") String email, @RequestParam(name = "username") String username,Model model) {
        if (playerDao.findByUsernameAndEmail(username, email) != null) {
            Player player = playerDao.findByUsernameAndEmail(username, email);
            model.addAttribute("player", player);
            return "redirect:/set/" + player.getId() + "/password";
        } else {
            return "redirect:/forgot-password?usernameError=true";
        }
    }

    @GetMapping("/set/{id}/password")
    public String showResetPassword(@PathVariable long id, Model model, @RequestParam(value = "passwordError", required = false) boolean passwordError, @RequestParam(value = "confirmError", required = false) boolean confirmError) {
        if (passwordError) {
            model.addAttribute("passwordError", passwordError);
        }
        if (confirmError) {
            model.addAttribute("confirmError", confirmError);
        }
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
            return "redirect:/set/" + changePlayer.getId() + "/password?passwordError=true";
        }
        else {
            if (password.equals(confirmPassword)) {
                String hash = passwordEncoder.encode(password);
                changePlayer.setPassword(hash);
                playerDao.save(changePlayer);
                return "redirect:/login";
            } else {
                return "redirect:/set/" + changePlayer.getId() + "/password?confirmError=true";
            }
        }
    }
}
