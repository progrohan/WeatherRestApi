package com.progrohan.weather.util;

import com.progrohan.weather.dto.UserLoginDTO;
import com.progrohan.weather.dto.UserRegRequestDTO;
import com.progrohan.weather.exception.InvalidDataException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DataValidator {

    public void checkUserReg(UserRegRequestDTO userRegRequestDTO){
        checkName(userRegRequestDTO.getLogin());
        checkPasswords(userRegRequestDTO.getPassword(), userRegRequestDTO.getConfirmPassword());
    }

    public void checkUserLogin(UserLoginDTO userLoginDTO){
        checkName(userLoginDTO.getLogin());
        checkPassword(userLoginDTO.getPassword());
    }

    public void checkName(String name){
        checkIfNull(name, "Name can not be empty!");
        checkIfSpaceExist(name, "Name can not contain space!");
    }

    public void checkPasswords(String firstPassword, String secondPassword){
        checkPassword(firstPassword);
        checkPassword(secondPassword);
        if (!Objects.equals(firstPassword, secondPassword))
            throw new InvalidDataException("Passwords must be equal!");
    }

    public void checkPassword(String password){
        checkIfNull(password, "Password can not be empty!");
        checkIfSpaceExist(password, "Password can not contain space!");
    }

    public void checkIfNull(String str, String msg){
        if (str.isEmpty()) throw new InvalidDataException(msg);
    }

    public void checkIfSpaceExist(String str, String msg){
        if(str.contains(" ")) throw new InvalidDataException(msg);
    }

}
