package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.EventRepository;
import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class EventController {

    private EventRepository eventsDao;
    private SportRepository sportsDao;

    public EventController(EventRepository eventsDao, SportRepository sportsDao) {
        this.eventsDao = eventsDao;
        this.sportsDao = sportsDao;
    }

    @GetMapping("/events")
    public String eventsIndex(Model model) {
        model.addAttribute("events", eventsDao.findAll());
        return "event/index";
    }

    @GetMapping("event/create")
    public String showCreateEvent(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("sports", sportsDao.findAll());
        return "event/create";
    }

    @PostMapping("event/create")
    public String createEvent(@ModelAttribute Event event) {
        eventsDao.save(event);
        return "redirect:/events";
    }

    @GetMapping("event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        model.addAttribute("event", eventsDao.getById(id));
        return "event/show";
    }

    @GetMapping(value = "/event/{id}/delete")
    public String deleteEvent(@ModelAttribute("event") Event event) {
        eventsDao.delete(event);
        return "redirect:/events";
    }

    @GetMapping(value = "/event/{id}/edit")
    public String showEditEvent(@PathVariable long id, Model model, HttpSession session) {
        session.setAttribute("id", id);
        model.addAttribute("sports", sportsDao.findAll());
        model.addAttribute("event", eventsDao.getById(id));
        return "event/edit";
    }

    @PostMapping(value = "event/{id}/edit")
    public String editEvent(@ModelAttribute("event") Event event, HttpSession session) {
        Long id = (Long) session.getAttribute("id");
<<<<<<< HEAD
        eventsDao.updateEvent(id, event.getTitle(), event.getDescription(), event.getLocation(), event.getStartTime(), event.getEndTime(), event.getDate(), event.getSport());
=======
//        eventsDao.updateEvent(id, event.getTitle(), event.getDescription(), event.getDate(), event.getStartTime(), event.getEndTime(), event.getLocation(), event.getSport());
>>>>>>> 2d7cedaaa7844ab360101a34e64ab0f754fb5dd0
        return "redirect:/events";
    }
}
