package com.progrohan.weather.dto.weather;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseDTO {

    private double temperature;

    private double feelsLike;

    private String weather;

    private String description;

    private double windSpeed;

    private int cloudiness;

}
