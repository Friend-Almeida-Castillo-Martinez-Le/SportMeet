package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Comment;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.CommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    private final CommentRepository commentDao;

    public CommentController(CommentRepository commentDao) {
        this.commentDao = commentDao;
    }

    @GetMapping("/comments")
    public String showComments(Model model){
        model.addAttribute("comments", commentDao.findAll());
        return "/comment/index";
    }

    @GetMapping("/comment/create")
    public String createComment(Model model){
        model.addAttribute("comment", new Comment());
        return "/comment/create";
    }

    @PostMapping("/comment/create")
    public String createCommentPart2(@ModelAttribute Comment comment){
        comment.setPlayer((Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        commentDao.save(comment);
        return "redirect:/comments";
    }
}
