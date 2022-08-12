package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.repositories.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    private EventRepository eventsDao;

    public EventController(EventRepository eventsDao) {
        this.eventsDao = eventsDao;
    }

    @GetMapping("/events")
    public String eventsIndex(Model model) {
        model.addAttribute("events", eventsDao.findAll());
        return "event/index";
    }

    @GetMapping("event/create")
    public String showCreateEvent(Model model) {
        model.addAttribute("event", new Event());
        return "event/create";
    }

    @PostMapping("event/create")
    public String createEvent(@ModelAttribute Event event) {
        eventsDao.save(event);
        return "redirect:/events";
    }

    @GetMapping("event/{id}/edit")
    public String editEvent(Model model, @PathVariable long id) {
        model.addAttribute("event", eventsDao.getById(id));
        return "event/show";
    }

    @GetMapping(value = "/event/{id}/delete")
    public String deleteEvent(@ModelAttribute("event") Event event) {
        eventsDao.delete(event);
        return "redirect:/events";
    }
}
