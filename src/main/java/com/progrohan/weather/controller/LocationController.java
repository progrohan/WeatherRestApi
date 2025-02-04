package com.progrohan.weather.controller;

import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/search")
    public List<LocationDTO> searchLocation(@RequestParam String name){
        return locationService.findLocationsByName(name);
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
