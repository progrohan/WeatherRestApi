package com.progrohan.weather.controller;

import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.UserLoginDTO;
import com.progrohan.weather.dto.UserRegRequestDTO;
import com.progrohan.weather.dto.UserResponseDTO;
import com.progrohan.weather.service.AuthService;
import com.progrohan.weather.util.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final DataValidator dataValidator;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegRequestDTO userRequestDTO){

        dataValidator.checkUserReg(userRequestDTO);

        UserResponseDTO userResponseDTO = authService.createUser(userRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody UserLoginDTO userLoginDTO){

        dataValidator.checkUserLogin(userLoginDTO);

        UserResponseDTO userResponseDTO = authService.login(userLoginDTO);

        SessionDTO session = authService.createSession(userResponseDTO);

        ResponseCookie cookie = ResponseCookie.from("sessionId", session.getUuid().toString())
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }

    @PostMapping("/logout")
    public String logOut(){
        return "Logged out successful";
    }

    @GetMapping("/users/me")
    public String getCurrentUser(){
        return "user";
    }

}
