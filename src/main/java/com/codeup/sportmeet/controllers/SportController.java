package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.EventRepository;
import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SportController {

    private final SportRepository sportDao;
    private final EventRepository eventsDao;

    public SportController(SportRepository sportDao, EventRepository eventsDao) {
        this.sportDao = sportDao;
        this.eventsDao = eventsDao;
    }

    @GetMapping("/sports")
    public String displaySports(Model model){
        model.addAttribute("sports", sportDao.findAll());
        return "sport/index";
    }

    @GetMapping("/sports/create/{name}")
    public String createSport(@PathVariable String name, Model model){
        sportDao.save(new Sport(name));
        return "redirect:/sports";
    }

    @GetMapping("/sport/{name}")
    public String viewSport(@PathVariable String name, Model model) {
        List<Event> eventsOnPage = new ArrayList<>();
        for (Event event: eventsDao.searchBySportName(name)) {
                eventsOnPage.add(event);
        }
        model.addAttribute("events", eventsOnPage);
        return "sport/show";
    }
}
