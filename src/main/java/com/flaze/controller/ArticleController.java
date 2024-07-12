package com.flaze.controller;

import com.flaze.DTO.ArticleDTO;
import com.flaze.DTO.GetUserDTO;
import com.flaze.exception.ArticleAlreadyExistException;
import com.flaze.exception.ArticleNotFoundException;
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


@Tag(name = "Article controllers")
@RestController
@RequestMapping("api/v1")
public class ArticleController {

    @Autowired
    private final ArticleService articleService;

    @Autowired
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/articles")
    public ResponseEntity homePage() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articleService.getAllArticles());
        } catch (Exception e) {
            Response response = new Response("Ошибка при получении списка статей", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/articles/add")
    public ResponseEntity addArticlePage(@RequestBody ArticleDTO articleDTO, Principal auth) {
        try {
            GetUserDTO user = userService.getUserByUsername(auth.getName());
            articleService.addArticle(articleDTO, user.getId());
            Response response = new Response("Статья успешно создана", 201);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ArticleAlreadyExistException e) {
            Response response = new Response("Статья с таким заголовком уже существует", 409);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (UserNotFoundException e) {
            Response response = new Response("Пользователь не найден", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity getUserPage(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticle(id));
        } catch (ArticleNotFoundException e) {
            Response response = new Response("Ошибка при получении статьи", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity deleteUserPage(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            Response response = new Response("Статься успешно удалена", 200);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ArticleNotFoundException e) {
            Response response = new Response("Ошибка при удалений статьи", 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
