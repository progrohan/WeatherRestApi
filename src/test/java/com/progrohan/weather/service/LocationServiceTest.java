package com.progrohan.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrohan.weather.config.TestConfig;
import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.UsersLocationDTO;
import com.progrohan.weather.dto.user.UserResponseDTO;
import com.progrohan.weather.exception.ApiException;
import com.progrohan.weather.mapper.LocationMapper;
import com.progrohan.weather.model.entity.Location;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.repository.LocationRepository;
import com.progrohan.weather.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
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
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Rollback
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Mock
    private Environment env;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;


    private final User TEST_USER = new User(null, "Name", "password");

    private final Location TEST_LOCATION = new Location(null, "Location", TEST_USER, BigDecimal.valueOf(1.1), BigDecimal.valueOf(43));

    private final UserResponseDTO TEST_USER_RESPONSE_DTO = new UserResponseDTO(1L, "Name");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findLocationsByName_ShouldReturnLocations() throws JsonProcessingException {
        String jsonResponse = """
                [
                    {
                        "name": "London",
                        "lat": 51.5073219,
                        "lon": -0.1276474,
                    }
                ]
                """;

        List<LocationDTO> locations = List.of(new LocationDTO(1L, "London",BigDecimal.valueOf( 51.5073219), BigDecimal.valueOf(-0.1276474)));

        when(env.getRequiredProperty("api.key")).thenReturn("test-key");
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);



        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class)))
                .thenReturn(locations);

        List<LocationDTO> locationsFinal = locationService.findLocationsByName("London");

        LocationDTO location = locations.get(0);

        assertAll(
                () -> assertNotNull(locationsFinal),
                () -> assertEquals(1, locationsFinal.size()),
                () -> assertEquals("London", location.getName()),
                () -> assertEquals( BigDecimal.valueOf(51.5073219), location.getLatitude()),
                () -> assertEquals(BigDecimal.valueOf(-0.1276474), location.getLongitude())
        );
    }

    @Test
    void findLocationsByName_ShouldThrowExceptionIf5xxError() {
        when(env.getRequiredProperty("api.key")).thenReturn("test-key");
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(ApiException.class, () -> locationService.findLocationsByName("London"));
    }

    @Test
    void saveLocation_ShouldSaveAndReturnLocation() {

        userRepository.save(TEST_USER);

        UsersLocationDTO locationDTO = new UsersLocationDTO(null, "Location", TEST_USER_RESPONSE_DTO, BigDecimal.valueOf(43), BigDecimal.valueOf(54));

        LocationDTO savedLocation = locationService.saveLocation(locationDTO);
        assertNotNull(savedLocation);
        assertEquals("Location", savedLocation.getName());
    }

    @Test
    void findByUserId_ShouldReturnUserLocations() {

        userRepository.save(TEST_USER);

        Location location = TEST_LOCATION;
        locationRepository.save(location);

        SessionDTO sessionDTO = new SessionDTO(UUID.randomUUID(), TEST_USER_RESPONSE_DTO, LocalDateTime.now().plusDays(3));
        List<LocationDTO> locations = locationService.findByUserId(sessionDTO);

        assertFalse(locations.isEmpty());
        assertEquals("Location", locations.get(0).getName());
    }

    @Test
    void delete_ShouldRemoveLocation() {

        userRepository.save(TEST_USER);

        Location location = TEST_LOCATION;
        location = locationRepository.save(location);

        locationService.delete(location.getId());
        Optional<Location> deletedLocation = locationRepository.findById(location.getId());

        assertTrue(deletedLocation.isEmpty());
    }
}
