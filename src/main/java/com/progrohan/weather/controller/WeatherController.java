package com.progrohan.weather.controller;

import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.weather.WeatherResponceDTO;
import com.progrohan.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponceDTO> getWeather(@RequestBody LocationDTO locationDTO){

        WeatherResponceDTO weather = weatherService.getByLocation(locationDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(weather);
    }

}
