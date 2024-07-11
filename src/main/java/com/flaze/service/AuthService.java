package com.flaze.service;

import com.flaze.DTO.*;
import com.flaze.exception.UserAlreadyExistException;
import com.flaze.response.Response;
import com.flaze.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            Response response = new Response("Неправильный логин или пароль", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody UserDTO registrationUserDto) throws UserAlreadyExistException {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            Response response = new Response("Пароли не совпадают", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            Response response = new Response("Пользователь с указанным именем уже существует", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        UserDTO user = userService.registerUser(registrationUserDto);
        return ResponseEntity.ok(new GetUserDTO(user.getUsername(), user.getAge(), user.getEmail()));
    }
}
