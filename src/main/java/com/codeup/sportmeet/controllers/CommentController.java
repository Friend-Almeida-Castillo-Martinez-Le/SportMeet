package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Comment;
import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.CommentRepository;
import com.codeup.sportmeet.repositories.EventRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    private final CommentRepository commentDao;
    private final EventRepository eventsDao;

    public CommentController(CommentRepository commentDao, EventRepository eventsDao) {
        this.commentDao = commentDao;
        this.eventsDao = eventsDao;
    }

    @GetMapping("/comments")
    public String showComments(Model model){
        model.addAttribute("comments", commentDao.findAll());
        return "/comment/index";
    }

    @GetMapping("/comment/{id}/create")
    public String createComment(Model model, @PathVariable long id){
        model.addAttribute("comment", new Comment());
        model.addAttribute("event", eventsDao.getById(id));
        return "/comment/create";
    }

    @PostMapping("/comment/{id}/create")
    public String createCommentPart2(@ModelAttribute Comment comment, @ModelAttribute("event") Event event){
        comment.setPlayer((Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(event.getId());
        comment.setEvent(eventsDao.getById(event.getId()));
        commentDao.save(comment);
        return "redirect:/event/" + event.getId();
    }
}
