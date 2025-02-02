package com.progrohan.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/register")
    public String registerUser(){
        return "name";
    }

    @PostMapping("/login")
    public String logIn(){
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
