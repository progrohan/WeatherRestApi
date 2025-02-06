package com.progrohan.weather.mapper;

import com.progrohan.weather.dto.weather.Weather;
import com.progrohan.weather.dto.weather.WeatherDTO;
import com.progrohan.weather.dto.weather.WeatherResponceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mappings({
            @Mapping(source = "main.temp", target = "temperature"),
            @Mapping(source = "main.feelsLike", target = "feelsLike"),
            @Mapping(source = "weather", target = "weather", qualifiedByName = "mapWeather"),
            @Mapping(source = "weather", target = "description", qualifiedByName = "mapDescription"),
            @Mapping(source = "wind.speed", target = "windSpeed"),
            @Mapping(source = "clouds.cloudiness", target = "cloudiness")
    })
    WeatherResponceDTO toDTO(WeatherDTO dto);

    @Named("mapWeather")
    default String mapWeather(List<Weather> weatherList) {
        return (weatherList != null && !weatherList.isEmpty()) ? weatherList.get(0).getMain() : "Unknown";
    }

    @Named("mapDescription")
    default String mapDescription(List<Weather> weatherList) {
        return (weatherList != null && !weatherList.isEmpty()) ? weatherList.get(0).getDescription() : "No description";
    }

}
