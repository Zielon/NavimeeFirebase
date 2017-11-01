package com.navimee.controllers;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.NavimeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class HomeController {

    @Autowired
    NavimeeRepository navimeeRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String home() {
        return NavimeeApplication.logs.stream().collect(Collectors.joining ("\n"));
    }
}
