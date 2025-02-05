package com.progrohan.weather.mapper;

import com.progrohan.weather.dto.LocationDTO;
import com.progrohan.weather.dto.UsersLocationDTO;
import com.progrohan.weather.model.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface LocationMapper {

    @Mapping(source = "user", target = "userId")
    Location toEntity(UsersLocationDTO dto);

    LocationDTO toDTO(Location entity);

    UsersLocationDTO toUsersDTO(LocationDTO dto);

}
