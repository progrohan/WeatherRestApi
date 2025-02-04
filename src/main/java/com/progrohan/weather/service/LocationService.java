package com.progrohan.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;




@Service
@RequiredArgsConstructor
public class LocationService {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public List<LocationDTO> findLocationsByName(String name){
        try{
            String url = String
                    .format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s",
                            name, env.getRequiredProperty("api.key")) ;

            String response = restTemplate.getForObject(url,  String.class);

            List<LocationDTO> locations = null;

            locations = objectMapper.readValue(response, new TypeReference<>() {});

            if (locations.isEmpty()) throw new ApiException("Nothing is found");

            return locations;
        } catch (JsonProcessingException e) {
            throw new ApiException("Problem with finding locations");
        }
    }

}
