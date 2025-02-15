package com.progrohan.weather.controller;

import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.weather.WeatherResponseDTO;
import com.progrohan.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<List<WeatherResponseDTO>> getWeather(@RequestBody List<LocationDTO> locationDTO){

        List<WeatherResponseDTO> weather = weatherService.getByLocation(locationDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(weather);
    }

}
