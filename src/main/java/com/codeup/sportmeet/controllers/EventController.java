package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.EventRepository;
import com.codeup.sportmeet.repositories.PlayerRepository;
import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    private EventRepository eventsDao;
    private SportRepository sportsDao;
    private PlayerRepository playerDao;

    public EventController(EventRepository eventsDao, SportRepository sportsDao, PlayerRepository playerDao) {
        this.playerDao = playerDao;
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
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        event.setPlayer(currentPlayer);
        Event event2 = eventsDao.save(event);
        if (playerDao.getById(currentPlayer.getId()).getEvents() == null) {
            playerDao.getById(currentPlayer.getId()).setEvents(new ArrayList<>());
            playerDao.getById(currentPlayer.getId()).getEvents().add(event2);
            System.out.println("EVENTS HERE ---> " + playerDao.getById(currentPlayer.getId()).getEvents());
        } else {
          playerDao.getById(currentPlayer.getId()).getEvents().add(event2);
          System.out.println("EVENTS HERE ---> " + playerDao.getById(currentPlayer.getId()).getEvents());
        }
        return "redirect:/events";
    }

    @GetMapping("event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        model.addAttribute("event", eventsDao.getById(id));
        return "event/show";
    }

    @PostMapping("event/{id}")
    public String attendEvent(@ModelAttribute("event") Event event) {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("HERE");
        System.out.println(currentPlayer);
        System.out.println(event);
        if (playerDao.getById(currentPlayer.getId()).getAttendingEvents() == null) {
            List<Event> events = new ArrayList<>();
            events.add(event);
            playerDao.getById(currentPlayer.getId()).setAttendingEvents(events);
        }
        else {
            playerDao.getById(currentPlayer.getId()).getAttendingEvents().add(event);
        }

        Event ev = eventsDao.getById(event.getId());
        if (eventsDao.getById(event.getId()).getPlayers() == null) {
            List<Player> players = new ArrayList<>();
            players.add(currentPlayer);
            ev.setPlayers(players);
        }
        else {
            ev.getPlayers().add(currentPlayer);
        }
        eventsDao.save(ev);
        System.out.println(currentPlayer.getEvents());
        return "redirect:/events";
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
        eventsDao.updateEvent(id, event.getTitle(), event.getDescription(), event.getLocation(), event.getStartTime(), event.getEndTime(), event.getDate(), event.getSport());
        return "redirect:/events";
    }
}
