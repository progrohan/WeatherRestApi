package com.progrohan.weather.service;

import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.weather.WeatherDTO;
import com.progrohan.weather.dto.weather.WeatherResponseDTO;
import com.progrohan.weather.mapper.WeatherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final WeatherMapper weatherMapper;

    public List<WeatherResponseDTO> getByLocation(List<LocationDTO> locations){

        List<WeatherResponseDTO> weatherResponseDTOS = new ArrayList<>();

        for (LocationDTO location: locations){
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                    location.getLatitude().toString(),
                    location.getLongitude().toString(),
                    env.getRequiredProperty("api.key"));

            WeatherDTO weather = restTemplate.getForObject(url, WeatherDTO.class);

            weatherResponseDTOS.add(weatherMapper.toDTO(weather));
        }

        return weatherResponseDTOS;

    }

}
