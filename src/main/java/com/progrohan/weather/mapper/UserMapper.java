package com.progrohan.weather.mapper;

import com.progrohan.weather.dto.UserLoginDTO;
import com.progrohan.weather.dto.UserRegistrationDTO;
import com.progrohan.weather.dto.UserResponseDTO;
import com.progrohan.weather.model.entity.User;
import com.progrohan.weather.util.PasswordEncoding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserLoginDTO dto);

    @Mapping(target = "password", expression = "java(passwordEncoding.encode(dto.getPassword()))")
    User toEntity(UserRegistrationDTO dto, PasswordEncoding passwordEncoding);

    UserResponseDTO toDto(User entity);

}
