package com.flaze.controller;


import com.flaze.exception.UserAlreadyExistException;
import com.flaze.exception.UserNotFoundException;
import com.flaze.response.Response;
import com.flaze.service.ArticleService;
import com.flaze.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Tag(name = "User controllers")
@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private final ArticleService articleService;
    @Autowired
    private final UserService userService;

    public UserController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity getUserPage(@PathVariable String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUsername(username));
        } catch (Exception e) {
            Response response = new Response("Пользователь не найден", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity getUsersPage() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        } catch (Exception e) {
            Response response = new Response("Ошибка при получении пользователя", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/users")
    public ResponseEntity updateUsernamePage(@PathVariable String newUsername, Principal auth) {
        try {
            Long id = userService.getUserByUsername(auth.getName()).getId();
            userService.updateUsername(id, newUsername);
            Response response = new Response("Username пользователя успешно обновлен", 200);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserNotFoundException e) {
            Response response = new Response("Пользователь не авторизован", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (UserAlreadyExistException e) {
            Response response = new Response("Пользователь под таким username-ом уже существует", 409);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
