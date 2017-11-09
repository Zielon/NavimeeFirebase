package com.navimee.controllers;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String home() {

        return NavimeeApplication.end; //placesRepository.getPlaces("SOPOT", FacebookPlace.class)
                //.stream()
                //.map(c -> String.format("%s", c.name))
                //.collect(Collectors.joining("\n"));
    }
}
