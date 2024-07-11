package com.flaze.service;

import com.flaze.DTO.ArticleDTO;
import com.flaze.DTO.GetArticleDTO;
import com.flaze.DTO.UserDTO;
import com.flaze.entity.ArticleEntity;
import com.flaze.entity.UserEntity;
import com.flaze.exception.ArticleAlreadyExistException;
import com.flaze.exception.ArticleNotFoundException;
import com.flaze.exception.UserNotAuthorizedException;
import com.flaze.exception.UserNotFoundException;
import com.flaze.repository.ArticleRepository;
import com.flaze.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private final ArticleRepository articleRepository;

    @Autowired
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<GetArticleDTO> getAllArticles() {
        List<ArticleEntity> articleEntities = articleRepository.findAll();

        List<GetArticleDTO> articles = new ArrayList<>();

        for (ArticleEntity article: articleEntities) {
            articles.add(GetArticleDTO.builder().
                    id(article.getId()).
                    title(article.getTitle()).
                    description(article.getDescription()).
                    text(article.getText()).
                    authorId(article.getAuthor().getId()).
                    build());
        }

        return articles;
    }

    public List<GetArticleDTO> getAllArticles(String username) {
        List<ArticleEntity> articleEntities = articleRepository.findByAuthorUsername(username);

        List<GetArticleDTO> articles = new ArrayList<>();

        for (ArticleEntity article: articleEntities) {
            articles.add(GetArticleDTO.builder().
                    id(article.getId()).
                    title(article.getTitle()).
                    description(article.getDescription()).
                    text(article.getText()).
                    authorId(article.getAuthor().getId()).
                    build());
        }

        return articles;
    }

    public ArticleEntity addArticle(ArticleDTO article, Long authorId) throws ArticleAlreadyExistException, UserNotAuthorizedException {
        if (articleRepository.existsByTitle(article.getTitle())) {
            throw new ArticleAlreadyExistException("Статья с таким заголовком уже существует");
        }

        Optional<UserEntity> userEntityOptional = userRepository.findById(authorId);
        if (userEntityOptional.isEmpty()) {
            throw new UserNotAuthorizedException("Пользователь с id " + authorId + " не найден");
        }

        UserEntity author = userEntityOptional.get();

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(article.getTitle());
        articleEntity.setDescription(article.getDescription());
        articleEntity.setText(article.getText());
        articleEntity.setAuthor(author);

        ArticleEntity savedArticle = articleRepository.save(articleEntity);

        author.getArticles().add(savedArticle);

        userRepository.save(author);

        return savedArticle;
    }

    public GetArticleDTO getArticle(Long id) throws ArticleNotFoundException {
        Optional<ArticleEntity> articleOptional = articleRepository.findById(id);
        if (articleOptional.isEmpty()) {
            throw new ArticleNotFoundException("Статья с id " + id + " не найдена");
        }

        ArticleEntity article = articleOptional.get();
        return GetArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .text(article.getText())
                .authorId(article.getAuthor().getId())
                .build();
    }

    public void deleteArticle(Long id) throws ArticleNotFoundException {
        Optional<ArticleEntity> articleOptional = articleRepository.findById(id);
        if (articleOptional.isEmpty()) {
            throw new ArticleNotFoundException("Статья с id " + id + " не найдена");
        }

        articleRepository.deleteById(id);
    }


}
