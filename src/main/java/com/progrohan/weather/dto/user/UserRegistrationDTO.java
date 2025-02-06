package com.progrohan.weather.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {

    private String login;

    private String password;

    private String confirmPassword;

}
