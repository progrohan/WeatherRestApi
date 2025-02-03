package com.progrohan.weather.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDTO {

    private UUID uuid;

    private UserResponseDTO user;

    private LocalDateTime expiresAt;

}
