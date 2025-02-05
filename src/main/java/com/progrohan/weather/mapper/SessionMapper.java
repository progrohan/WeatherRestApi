package com.progrohan.weather.mapper;

import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.model.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, UUIDMapper.class})
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mappings({
            @Mapping(source = "uuid", target = "id", qualifiedByName = "uuidToString"),
            @Mapping(source = "user", target = "userId"),
    })
    Session toEntity(SessionDTO dto);

    @Mappings({
            @Mapping(source = "id", target = "uuid", qualifiedByName = "stringToUuid"),
            @Mapping(source = "userId", target = "user"),
    })
    SessionDTO toDto(Session entity);

}
