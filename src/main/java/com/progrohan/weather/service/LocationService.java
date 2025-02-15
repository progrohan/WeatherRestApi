package com.progrohan.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.UsersLocationDTO;
import com.progrohan.weather.exception.ApiException;
import com.progrohan.weather.exception.DataExistException;
import com.progrohan.weather.exception.DataNotFoundException;
import com.progrohan.weather.mapper.LocationMapper;
import com.progrohan.weather.mapper.SessionMapper;
import com.progrohan.weather.model.entity.Location;
import com.progrohan.weather.model.entity.Session;
import com.progrohan.weather.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LocationService {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final SessionMapper sessionMapper;


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
        } catch (Exception e) {
            throw new ApiException("Problem with finding locations");
        }
    }

    public LocationDTO saveLocation(UsersLocationDTO locationDTO){
        try {
            Location location = locationRepository.save(locationMapper.toEntity(locationDTO));

            return locationMapper.toDTO(location);

        }catch(DataExistException e){
            throw new DataExistException("Location is already exists");
        }
    }

    public List<LocationDTO> findByUserId(SessionDTO sessionDTO){

        Session session = sessionMapper.toEntity(sessionDTO);

        List<Location> locations = locationRepository
                .findByUserID( session.getUserId());

        List<LocationDTO> locationDTOS = locations
                .stream()
                .map(locationMapper::toDTO)
                .toList();

        return locationDTOS;

    }

    public void delete(Long id){

        locationRepository.delete(id);

    }

    public boolean checkUsersPermission(SessionDTO session, Long id){

        Optional<Location> locationOptional = locationRepository.findById(id);

        if (locationOptional.isEmpty()){
            throw new DataNotFoundException("Location with id "
                                            + id + " not exists");
        }else{
            Location location = locationOptional.get();

            Long locationOwnersId = location.getUserId().getId();

            Long currentUserId = session.getUser().getId();

            return Objects.equals(locationOwnersId, currentUserId);
        }


    }

}
