package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Comment;
import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.CommentRepository;
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
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    private EventRepository eventsDao;
    private SportRepository sportsDao;
    private PlayerRepository playersDao;
    private CommentRepository commentsDao;

    public EventController(EventRepository eventsDao, SportRepository sportsDao, PlayerRepository playerDao, CommentRepository commentsDao) {
        this.playersDao = playerDao;
        this.eventsDao = eventsDao;
        this.sportsDao = sportsDao;
        this.commentsDao = commentsDao;
    }

    @GetMapping("/events")
    public String eventsIndex(Model model) throws ParseException {
        model.addAttribute("events", eventsDao.findAll());
        for (Event event : eventsDao.findAll()) {
            String eventDate = event.getDate();
            String year = eventDate.substring(0,4);
            String month = eventDate.substring(4,6);
            String day = eventDate.substring(6,8);
            if (month.split("")[0].equals("0")) {
                month = month.split("")[1];
                month.join("");
            }
            LocalDate date = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month), Integer.parseInt(day));
            model.addAttribute("date", String.format("%s %d, %d",date.getMonth(), date.getDayOfMonth(), date.getYear()));

        }
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
        if (playersDao.getById(currentPlayer.getId()).getEvents() == null) {
            playersDao.getById(currentPlayer.getId()).setEvents(new ArrayList<>());
            playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
            System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
        } else {
          playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
          System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
        }
        return "redirect:/events";
    }

    @GetMapping("event/{id}")
    public String showEvent(Model model, @PathVariable long id) {
        model.addAttribute("event", eventsDao.getById(id));
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", eventsDao.getById(id).getComments());
        return "event/show";
    }

    @PostMapping("event/{id}/attend")
    public String attendEvent(@ModelAttribute("event") Event event, Model model) throws Exception {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Player pl = playersDao.getById(currentPlayer.getId());
        Event ev = eventsDao.getById(event.getId());
        if (playersDao.getById(currentPlayer.getId()).getAttendingEvents() == null) {
            List<Event> events = new ArrayList<>();
            events.add(event);
            pl.setAttendingEvents(events);
        }
        else {
            pl.getAttendingEvents().add(event);
        }
        playersDao.save(pl);

        if (eventsDao.getById(event.getId()).getPlayers() == null) {
            List<Player> players = new ArrayList<>();
            players.add(currentPlayer);
            ev.setPlayers(players);
            ev.setPlayersAttending(eventsDao.getById(event.getId()).getPlayers().size());
        }
        else {
            ev.getPlayers().add(currentPlayer);
            ev.setPlayersAttending(eventsDao.getById(event.getId()).getPlayers().size());
        }
        eventsDao.save(ev);
        System.out.println(currentPlayer.getEvents());
        return "redirect:/event/" + ev.getId();
    }

    @GetMapping(value = "/event/{id}/delete")
    public String deleteEvent(@ModelAttribute("event") Event event) {
        for (Comment comment : commentsDao.findAll()) {
            long commentEventID = comment.getEvent().getId();
            if (commentEventID == event.getId()) {
                commentsDao.delete(comment);
            }
        }
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
