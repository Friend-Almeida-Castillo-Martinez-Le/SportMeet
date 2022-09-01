package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Comment;
import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.CommentRepository;
import com.codeup.sportmeet.repositories.EventRepository;
import com.codeup.sportmeet.repositories.PlayerRepository;
import com.codeup.sportmeet.repositories.SportRepository;
import org.hibernate.type.LocalTimeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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

    @Value("${FILESTACK_API}")
    private String fsKey;

    @Value("${MAPBOX_API}")
    private String mbKey;

    @Value("${WEATHER_API}")
    private String wKey;

    @GetMapping("events")
    public String eventsIndex(Model model) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        Date todayAsDate = format.parse(today);
        LocalTime now = LocalTime.now();
        List<Event> orderedByDateAndTime = eventsDao.orderEventsByDateAndStartTime(eventsDao.findAll());
        List<Event> eventsOrdered = new ArrayList<>();
        for (Event event : orderedByDateAndTime) {
            if (format.parse(event.getDate()).after(todayAsDate)) {
                eventsOrdered.add(event);
            } else if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) > now.getHour()) {
                eventsOrdered.add(event);
            } else if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) == now.getHour() && Integer.parseInt(event.getStartTime().substring(3, 5)) > now.getMinute()) {
                eventsOrdered.add(event);
            } else {
                continue;
            }
        }
        model.addAttribute("events", eventsOrdered);
        return "event/index";
    }

    @GetMapping("event/create")
    public String showCreateEvent(Model model) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            model.addAttribute("event", new Event());
            model.addAttribute("sports", sportsDao.findAll());
            model.addAttribute("fsKey", fsKey);
            return "event/create";
        }
    }

    @PostMapping("event/create")
    public String createEvent(@ModelAttribute Event event, @RequestParam("profile_img") String url) throws ParseException {
        Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        Date todayAsDate = format.parse(today);
        LocalTime now = LocalTime.now();
        if (format.parse(event.getDate()).after(todayAsDate)) {
            event.setPlayer(currentPlayer);
            if (!url.equals("")) {
                event.setEventPicUrl(url);
            }
            Event event2 = eventsDao.save(event);
            if (playersDao.getById(currentPlayer.getId()).getEvents() == null) {
                playersDao.getById(currentPlayer.getId()).setEvents(new ArrayList<>());
                playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
                System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
                return "redirect:/events";
            } else {
                playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
                System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
                return "redirect:/events";
            }
        } else if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) > now.getHour()) {
            event.setPlayer(currentPlayer);
            if (!url.equals("")) {
                event.setEventPicUrl(url);
            }
            Event event2 = eventsDao.save(event);
            if (playersDao.getById(currentPlayer.getId()).getEvents() == null) {
                playersDao.getById(currentPlayer.getId()).setEvents(new ArrayList<>());
                playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
                System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
                return "redirect:/events";
            } else {
                playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
                System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
                return "redirect:/events";
            }
        } else if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) == now.getHour() && Integer.parseInt(event.getStartTime().substring(3, 5)) > now.getMinute()) {
            event.setPlayer(currentPlayer);
            if (!url.equals("")) {
                event.setEventPicUrl(url);
            }
            Event event2 = eventsDao.save(event);
            if (playersDao.getById(currentPlayer.getId()).getEvents() == null) {
                playersDao.getById(currentPlayer.getId()).setEvents(new ArrayList<>());
                playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
                System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
                return "redirect:/events";
            } else {
                playersDao.getById(currentPlayer.getId()).getEvents().add(event2);
                System.out.println("EVENTS HERE ---> " + playersDao.getById(currentPlayer.getId()).getEvents());
                return "redirect:/events";
            }
        } else {
            return "redirect:/event/create";
        }
    }

    @GetMapping("event/{id}")
    public String showEvent(Model model, @PathVariable long id) throws ParseException {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {

            Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Event event = eventsDao.getById(id);
            List<String> eventPlayerUsernames = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String today = format.format(new Date());
            Date todayAsDate = format.parse(today);
            LocalTime now = LocalTime.now();
            if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) <= now.getHour() && Integer.parseInt(event.getStartTime().substring(3, 5)) <= now.getMinute()) {
                System.err.println(Integer.parseInt(event.getStartTime().substring(3, 5)));
                System.err.println(now.getMinute());
                event.setStarted(true);
                eventsDao.save(event);
            }
            for (Player player : eventsDao.getById(id).getPlayers()) {
                eventPlayerUsernames.add(player.getUsername());
            }
            model.addAttribute("event", event);
            model.addAttribute("comment", new Comment());
            model.addAttribute("comments", eventsDao.getById(id).getComments());
            model.addAttribute("eventPlayerUsernames", eventPlayerUsernames);
            model.addAttribute("mbKey", mbKey);
            model.addAttribute("wKey", wKey);
            if (event.getLocation() == null) {
                event.setLocation("San Antonio, TX");
                model.addAttribute("geocodeLoc", event.getLocation());
            } else {
                model.addAttribute("geocodeLoc", event.getLocation());
            }
            model.addAttribute("eventDate", event.getDate());
            return "event/show";
        }
    }

    @PostMapping("event/{id}/attend")
    public String attendEvent(@ModelAttribute("event") Event event, Model model) throws Exception {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Player currentPlayer = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Player pl = playersDao.getById(currentPlayer.getId());
            Event ev = eventsDao.getById(event.getId());
            if (playersDao.getById(currentPlayer.getId()).getAttendingEvents() == null) {
                List<Event> events = new ArrayList<>();
                events.add(event);
                pl.setAttendingEvents(events);
            } else {
                pl.getAttendingEvents().add(event);
            }
            playersDao.save(pl);

            if (eventsDao.getById(event.getId()).getPlayers() == null) {
                List<Player> players = new ArrayList<>();
                players.add(currentPlayer);
                ev.setPlayers(players);
                ev.setPlayersAttending(eventsDao.getById(event.getId()).getPlayers().size());
            } else {
                ev.getPlayers().add(currentPlayer);
                ev.setPlayersAttending(eventsDao.getById(event.getId()).getPlayers().size());
            }
            eventsDao.save(ev);
            System.out.println(currentPlayer.getEvents());
            return "redirect:/event/" + ev.getId();
        }
    }

    @GetMapping(value = "/event/{id}/delete")
    public String deleteEvent(@ModelAttribute("event") Event event) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            for (Comment comment : commentsDao.findAll()) {
                long commentEventID = comment.getEvent().getId();
                if (commentEventID == event.getId()) {
                    commentsDao.delete(comment);
                }
            }
            eventsDao.delete(event);
            return "redirect:/events";
        }
    }

    @GetMapping(value = "/event/{id}/edit")
    public String showEditEvent(@PathVariable long id, Model model, HttpSession session) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            session.setAttribute("id", id);
            model.addAttribute("sports", sportsDao.findAll());
            model.addAttribute("event", eventsDao.getById(id));
            return "event/edit";
        }
    }

    @PostMapping(value = "event/{id}/edit")
    public String editEvent(@ModelAttribute("event") Event event, HttpSession session) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Long id = (Long) session.getAttribute("id");
            eventsDao.updateEvent(id, event.getTitle(), event.getDescription(), event.getLocation(), event.getStartTime(), event.getDate(), event.getSport());
            return "redirect:/events";
        }
    }

    @GetMapping("event/search")
    public String showSearchedEvents(Model model, @RequestParam("search") String search) throws ParseException {
        List<Event> searchedEvents = new ArrayList<>();
        for (Event event : eventsDao.searchEvents(search)) {
            searchedEvents.add(event);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        Date todayAsDate = format.parse(today);
        LocalTime now = LocalTime.now();
        List<Event> eventsOrdered = new ArrayList<>();
        for (Event event : searchedEvents) {
            if (format.parse(event.getDate()).after(todayAsDate)) {
                eventsOrdered.add(event);
            } else if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) > now.getHour()) {
                eventsOrdered.add(event);
            } else if (format.parse(event.getDate()).equals(todayAsDate) && Integer.parseInt(event.getStartTime().substring(0, 2)) == now.getHour() && Integer.parseInt(event.getStartTime().substring(3, 5)) > now.getMinute()) {
                eventsOrdered.add(event);
            } else {
                continue;
            }
        }
        model.addAttribute("events", eventsOrdered);
        return "event/search";
    }

    @GetMapping("/event/{id}/upload")
    public String viewAddProfilePhoto(@PathVariable long id, Model model, HttpSession session) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            session.setAttribute("id", id);
            model.addAttribute("fsKey", fsKey);
            model.addAttribute("event", eventsDao.getById(id));
            return "event/upload";
        }
    }

    @PostMapping("/event/{id}/upload")
    public String savePhoto(@RequestParam(name = "profile_img") String img, @PathVariable long id) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Event event = eventsDao.getById(id);
            event.setEventPicUrl(img);
            eventsDao.save(event);
            return "redirect:/event/" + id;
        }
    }

    @GetMapping("/event/{id}/start")
    public String showStart(@PathVariable long id, Model model) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Event event = eventsDao.getById(id);
            model.addAttribute("event", event);
            model.addAttribute("players", event.getPlayers());
            return "event/start";
        }
    }

    @PostMapping("/event/{id}/start")
    public String submitStart(@ModelAttribute("event") Event event, @ModelAttribute("players") ArrayList<Player> players, @RequestParam("confirmedPlayers") List<Long> confirmedPlayers) {
        if (String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        } else {
            Event event1 = eventsDao.getById(event.getId());
            event1.setConfirmed(true);
            List<Player> newPlayers = new ArrayList<>();
            for (Long id : confirmedPlayers) {
                newPlayers.add(playersDao.getById(id));
            }
            event1.setPlayers(newPlayers);
            event1.setPlayersAttending(eventsDao.getById(event1.getId()).getPlayers().size());
            eventsDao.save(event1);
            return "redirect:/event/" + event1.getId();
        }
    }
}
