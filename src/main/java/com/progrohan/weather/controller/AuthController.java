package com.progrohan.weather.controller;

import com.progrohan.weather.dto.UserLoginDTO;
import com.progrohan.weather.dto.UserRegRequestDTO;
import com.progrohan.weather.dto.UserResponseDTO;
import com.progrohan.weather.service.AuthService;
import com.progrohan.weather.util.DataValidator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final DataValidator dataValidator;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegRequestDTO userRequestDTO){

        dataValidator.checkUserReg(userRequestDTO);

        UserResponseDTO userResponseDTO = authService.create(userRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public String logIn(@RequestBody UserLoginDTO userLoginDTO){

        dataValidator.checkUserLogin(userLoginDTO);



        return "Login successful";
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
