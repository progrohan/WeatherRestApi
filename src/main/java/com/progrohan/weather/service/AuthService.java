package com.progrohan.weather.service;

import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.user.UserLoginDTO;
import com.progrohan.weather.dto.user.UserRegistrationDTO;
import com.progrohan.weather.dto.user.UserResponseDTO;
import com.progrohan.weather.exception.DataExistException;
import com.progrohan.weather.exception.DataNotFoundException;
import com.progrohan.weather.exception.InvalidDataException;
import com.progrohan.weather.exception.SessionException;
import com.progrohan.weather.mapper.SessionMapper;
import com.progrohan.weather.mapper.UserMapper;
import com.progrohan.weather.model.entity.Session;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.repository.SessionRepository;
import com.progrohan.weather.repository.UserRepository;
import com.progrohan.weather.util.PasswordEncoding;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final UserMapper userMapper;
    private final PasswordEncoding passwordEncoding;

    public UserResponseDTO createUser(UserRegistrationDTO userRequestDTO){
        try{
            User user = userMapper.toEntity(userRequestDTO, passwordEncoding);

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

    public void deleteSession(SessionDTO sessionDTO){

        sessionRepository.delete(sessionDTO.getUuid().toString());

    }

    public UserResponseDTO login(UserLoginDTO userLoginDTO){

        Optional<User> userOptional = userRepository.findByName(userLoginDTO.getLogin());

        User user = userOptional.orElseThrow(() -> new DataNotFoundException("User not found!"));

        if(!passwordEncoding.checkPassword(userLoginDTO.getPassword(), user.getPassword()))
            throw new InvalidDataException("Password is incorrect!");

        return userMapper.toDto(user);
    }

    public SessionDTO getSessionFromCookies(Cookie[] cookies){
        try {
            String sessionId = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("sessionId".equals(cookie.getName())) {
                        sessionId = cookie.getValue();
                        break;
                    }
                }
            } else throw new SessionException("Cookie is empty");

            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);

            Session session = sessionOptional.orElseThrow(() -> new SessionException("Session not found!"));

            deleteSessionIfExpires(session);

            return sessionMapper.toDto(session);
        }catch (Exception e){
            throw new SessionException("Problem with current session");
        }

    }

    private void deleteSessionIfExpires(Session session){

        if(session.getExpiresAt().isBefore(LocalDateTime.now())){
            sessionRepository.delete(session.getId());
            throw new SessionException("Session has been expired");
        }
    }
}
