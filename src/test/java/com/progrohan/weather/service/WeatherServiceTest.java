package com.progrohan.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrohan.weather.config.TestConfig;
import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.UsersLocationDTO;
import com.progrohan.weather.dto.user.UserResponseDTO;
import com.progrohan.weather.dto.weather.WeatherDTO;
import com.progrohan.weather.dto.weather.WeatherResponseDTO;
import com.progrohan.weather.exception.ApiException;
import com.progrohan.weather.mapper.WeatherMapper;
import com.progrohan.weather.model.entity.Location;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.repository.LocationRepository;
import com.progrohan.weather.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Rollback
public class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private Environment env;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByLocation() {

        LocationDTO location1 = new LocationDTO(1L, "Rostov",new BigDecimal("40.7128"), new BigDecimal("-74.0060"));
        LocationDTO location2 = new LocationDTO(2L, "Moscow",new BigDecimal("34.0522"), new BigDecimal("-118.2437"));
        List<LocationDTO> locations = List.of(location1, location2);

        WeatherDTO mockWeather = new WeatherDTO();
        WeatherResponseDTO mockResponse = new WeatherResponseDTO();

        when(restTemplate.getForObject(anyString(), eq(WeatherDTO.class))).thenReturn(mockWeather);
        when(weatherMapper.toDTO(mockWeather)).thenReturn(mockResponse);


        List<WeatherResponseDTO> result = weatherService.getByLocation(locations);


        assertNotNull(result);
        assertEquals(2, result.size());


        verify(restTemplate, times(2)).getForObject(anyString(), eq(WeatherDTO.class));
        verify(weatherMapper, times(2)).toDTO(mockWeather);
    }
}

