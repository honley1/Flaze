package com.flaze.controller;

import com.flaze.DTO.JwtRequest;
import com.flaze.DTO.UserDTO;
import com.flaze.exception.UserAlreadyExistException;
import com.flaze.exception.UserNotFoundException;
import com.flaze.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flaze.service.AuthService;


@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/jwt")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity createNewUser(@RequestBody UserDTO registrationUserDto) throws UserNotFoundException, UserAlreadyExistException {
        try {
            return authService.createNewUser(registrationUserDto);
        } catch (UserAlreadyExistException e) {
            Response response = new Response("Пользователь с таким ником уже существует", 409);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}