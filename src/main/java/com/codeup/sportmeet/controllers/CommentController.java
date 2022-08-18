package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Comment;
import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.CommentRepository;
import com.codeup.sportmeet.repositories.EventRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        Comment newComment = new Comment();
        newComment.setPlayer((Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        newComment.setDescription(comment.getDescription());
        newComment.setEvent(eventsDao.getById(event.getId()));
        comment.setEvent(eventsDao.getById(event.getId()));
        commentDao.save(newComment);
        return "redirect:/event/" + event.getId();
    }
}
