package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Sport;
import com.codeup.sportmeet.repositories.EventRepository;
import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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
    public String viewSport(@PathVariable String name, Model model) throws ParseException {
        List<Event> eventsOnPage = new ArrayList<>();
        for (Event event: eventsDao.searchBySportName(name)) {
                eventsOnPage.add(event);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        Date todayAsDate = format.parse(today);
        LocalTime now = LocalTime.now();
        List<Event> eventsOrdered = new ArrayList<>();
        for (Event event : eventsOnPage) {
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
        return "sport/show";
    }
}
