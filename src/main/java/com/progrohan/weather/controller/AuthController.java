package com.progrohan.weather.controller;

import com.progrohan.weather.dto.SessionDTO;
import com.progrohan.weather.dto.user.UserLoginDTO;
import com.progrohan.weather.dto.user.UserRegistrationDTO;
import com.progrohan.weather.dto.user.UserResponseDTO;
import com.progrohan.weather.service.AuthService;
import com.progrohan.weather.util.DataValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Methods for auth")
public class AuthController {

    private final AuthService authService;
    private final DataValidator dataValidator;

    @PostMapping("/register")
    @Operation(summary = "Registrates user")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userRequestDTO){

        dataValidator.checkUserReg(userRequestDTO);

        UserResponseDTO userResponseDTO = authService.createUser(userRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody UserLoginDTO userLoginDTO){

        dataValidator.checkUserLogin(userLoginDTO);

        UserResponseDTO userResponseDTO = authService.login(userLoginDTO);

        SessionDTO session = authService.createSession(userResponseDTO);

        ResponseCookie cookie = ResponseCookie.from("sessionId", session.getUuid().toString())
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut(HttpServletRequest request){

        SessionDTO session = authService.getSessionFromCookies(request.getCookies());

        authService.deleteSession(session);

        ResponseCookie cookie = ResponseCookie.from("sessionId", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(HttpServletRequest request){

        SessionDTO session = authService.getSessionFromCookies(request.getCookies());

        return ResponseEntity.status(HttpStatus.OK).body(session.getUser());
    }

}
