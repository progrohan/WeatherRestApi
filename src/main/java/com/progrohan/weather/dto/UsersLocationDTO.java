package com.progrohan.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersLocationDTO {

    private Long id;

    private String name;

    private UserResponseDTO user;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
