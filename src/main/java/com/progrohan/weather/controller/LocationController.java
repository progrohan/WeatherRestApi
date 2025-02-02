package com.progrohan.weather.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @GetMapping("/search")
    public String searchLocation(){
        return "location";
    }

    @PostMapping
    public String addLocation(){
        return "Added location";
    }

    @GetMapping
    public String getUserLocations(){
        return "users locations";
    }

    @DeleteMapping("/{id}")
    public String deleteLocation(){
        return "deleted location";
    }

}
