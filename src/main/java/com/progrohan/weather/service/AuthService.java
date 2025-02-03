package com.progrohan.weather.service;

import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.UserLoginDTO;
import com.progrohan.weather.dto.UserRegRequestDTO;
import com.progrohan.weather.dto.UserResponseDTO;
import com.progrohan.weather.exception.DataExistException;
import com.progrohan.weather.exception.DataNotFoundException;
import com.progrohan.weather.exception.InvalidDataException;
import com.progrohan.weather.mapper.SessionMapper;
import com.progrohan.weather.mapper.UserMapper;
import com.progrohan.weather.model.entity.Session;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.repository.SessionRepository;
import com.progrohan.weather.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRegRequestDTO userRequestDTO){
        try{
            User user = userMapper.toEntity(userRequestDTO);

            user = userRepository.save(user);

            return userMapper.toDto(user);
        }catch(DataExistException e){
            throw new DataExistException("User with name" + userRequestDTO.getLogin() + " already exists");
        }

    }

    public SessionDTO createSession(UserResponseDTO userResponseDTO){
        LocalDateTime time = LocalDateTime.now();

        SessionDTO sessionDTO = SessionDTO.builder()
                .uuid(UUID.randomUUID())
                .user(userResponseDTO)
                .expiresAt(time.plusDays(7))
                .build();

        Session session = sessionRepository.save(sessionMapper.toEntity(sessionDTO));

        return sessionMapper.toDto(session);
    }

    public UserResponseDTO login(UserLoginDTO userLoginDTO){

        Optional<User> userOptional = userRepository.findByName(userLoginDTO.getLogin());

        User user = userOptional.orElseThrow(() -> new DataNotFoundException("User not found!"));

        if(!Objects.equals(user.getPassword(), userLoginDTO.getPassword()))
            throw new InvalidDataException("Password is incorrect!");

        return userMapper.toDto(user);
    }

    public SessionDTO getSessionFromCookies(Cookie[] cookies){

        String sessionId = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        } else throw new InvalidDataException("Cookie is empty");

        Optional<Session> sessionOptional = sessionRepository.findById(UUID.fromString(sessionId));

        Session session = sessionOptional.orElseThrow(() -> new DataNotFoundException("Session not found!"));

        return sessionMapper.toDto(session);
    }

}
