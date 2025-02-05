package com.progrohan.weather.controller;

import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.UsersLocationDTO;
import com.progrohan.weather.mapper.LocationMapper;
import com.progrohan.weather.service.AuthService;
import com.progrohan.weather.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final AuthService authService;
    private final LocationMapper locationMapper;

    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>>  searchLocation(@RequestParam String name){
        List<LocationDTO> locations = locationService.findLocationsByName(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locations);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> addLocation(
            @RequestBody LocationDTO locationDTO,
            HttpServletRequest request){

        SessionDTO session = authService.getSessionFromCookies(request.getCookies());

        UsersLocationDTO usersLocationDTO = locationMapper.toUsersDTO(locationDTO);

        usersLocationDTO.setUser(session.getUser());

        LocationDTO location = locationService.saveLocation(usersLocationDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(location);
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
