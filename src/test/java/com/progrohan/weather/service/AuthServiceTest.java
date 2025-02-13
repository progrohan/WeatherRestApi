package com.progrohan.weather.service;

import com.progrohan.weather.config.TestConfig;
import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.user.UserRegistrationDTO;
import com.progrohan.weather.dto.user.UserResponseDTO;
import com.progrohan.weather.exception.DataExistException;
import com.progrohan.weather.exception.SessionException;
import com.progrohan.weather.model.entity.Session;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.repository.SessionRepository;
import com.progrohan.weather.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void testCreateUser_Success() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO("testUser", "password123", "password123");

        UserResponseDTO responseDTO = authService.createUser(userDTO);

        assertNotNull(responseDTO);
        assertEquals("testUser", responseDTO.getLogin());

        Optional<User> savedUser = userRepository.findByName("testUser");
        assertTrue(savedUser.isPresent());
    }

    @Test
    public void testCreateUser_DuplicateLogin_ThrowsException() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO("existingUser", "password123", "password123");
        authService.createUser(userDTO);

        assertThrows(DataExistException.class, () -> authService.createUser(userDTO));
    }

    @Test
    public void testCreateSession_Success() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO("testUser", "password123", "password123");
        UserResponseDTO userResponse = authService.createUser(userDTO);

        SessionDTO sessionDTO = authService.createSession(userResponse);

        assertNotNull(sessionDTO);
        assertEquals(userResponse.getLogin(), sessionDTO.getUser().getLogin());

        Optional<Session> session = sessionRepository.findById(sessionDTO.getUuid().toString());
        assertTrue(session.isPresent());
    }

    @Test
    public void testDeleteSession_Success() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO("testUser", "password123", "password123");
        UserResponseDTO userResponse = authService.createUser(userDTO);
        SessionDTO sessionDTO = authService.createSession(userResponse);

        authService.deleteSession(sessionDTO);

        Optional<Session> session = sessionRepository.findById(sessionDTO.getUuid().toString());
        assertFalse(session.isPresent());
    }

    @Test
    public void testSessionExpiration() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO("testUser", "password123", "password123");
        UserResponseDTO userResponse = authService.createUser(userDTO);


        Session session =   Session.builder()
                .id(UUID.randomUUID().toString())
                .userId(new User(userResponse.getId(), userResponse.getLogin(), null))
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .build();
        sessionRepository.save(session);

        assertThrows(SessionException.class, () -> authService.getSessionFromCookies(new Cookie[]{new Cookie("sessionId", session.getId())}));
    }

}


