package com.progrohan.weather.mapper;

import com.progrohan.weather.dto.UserLoginDTO;
import com.progrohan.weather.dto.UserRegRequestDTO;
import com.progrohan.weather.dto.UserResponseDTO;
import com.progrohan.weather.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserLoginDTO dto);

    User toEntity(UserRegRequestDTO dto);

    UserResponseDTO toDto(User entity);

}
