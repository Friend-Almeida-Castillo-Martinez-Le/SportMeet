package com.codeup.sportmeet.controllers;

import com.codeup.sportmeet.repositories.SportRepository;
import org.springframework.stereotype.Controller;

@Controller
public class SportController {

    private final SportRepository sportDao;

    public SportController(SportRepository sportDao) {
        this.sportDao = sportDao;
    }
}
