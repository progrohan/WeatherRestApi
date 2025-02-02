package com.progrohan.weather.service;

import com.progrohan.weather.dto.UserRegRequestDTO;
import com.progrohan.weather.dto.UserResponseDTO;
import com.progrohan.weather.exception.DataExistException;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public UserResponseDTO create(UserRegRequestDTO userRequestDTO){
        try{
            User user = new User(null, userRequestDTO.getLogin(), userRequestDTO.getPassword());

            user = userRepository.save(user);

            return new UserResponseDTO(user.getLogin());
        }catch(DataExistException e){
            throw new DataExistException("User with name" + userRequestDTO.getLogin() + " already exists");
        }

    }

}
