package com.progrohan.weather.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegRequestDTO {

    private String login;

    private String password;

    private String confirmPassword;

}
