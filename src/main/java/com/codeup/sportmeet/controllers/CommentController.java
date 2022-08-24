package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Comment;
import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.repositories.CommentRepository;
import com.codeup.sportmeet.repositories.EventRepository;
import com.codeup.sportmeet.repositories.PlayerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    private final CommentRepository commentDao;
    private final EventRepository eventsDao;
    private final PlayerRepository playersDao;

    public CommentController(CommentRepository commentDao, EventRepository eventsDao, PlayerRepository playersDao) {
        this.commentDao = commentDao;
        this.eventsDao = eventsDao;
        this.playersDao = playersDao;
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

    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@ModelAttribute("comment") Comment comment) {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Player current = playersDao.getById(currentPlayer.getId());
        Comment currentComment = commentDao.getById(comment.getId());
        if (playersDao.getById(currentComment.getPlayer().getId()).getId() == current.getId() || playersDao.getById(current.getId()).getId() == eventsDao.getById(currentComment.getEvent().getId()).getPlayer().getId()) {
            commentDao.delete(commentDao.getById(currentComment.getId()));
        }
        return "redirect:/event/" + currentComment.getEvent().getId();
    }
}
