package com.progrohan.weather.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UUIDMapper {

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    default UUID stringToUuid(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }

}
