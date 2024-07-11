package com.flaze.service;

import com.flaze.DTO.GetArticleDTO;
import com.flaze.DTO.GetUserDTO;
import com.flaze.DTO.UserDTO;
import com.flaze.config.SecurityConfig;
import com.flaze.entity.UserEntity;
import com.flaze.exception.UserAlreadyExistException;
import com.flaze.exception.UserNotFoundException;
import com.flaze.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ArticleService articleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ArticleService articleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.articleService = articleService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(UserDTO user) throws UserAlreadyExistException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("Пользователь под таким username-ом уже существует");
        }

        UserEntity userEntity = userRepository.save(UserEntity.builder().
                username(user.getUsername()).
                password(passwordEncoder.encode(user.getPassword())).
                age(user.getAge()).
                email(user.getEmail()).
                build());

        return UserDTO.builder().
                username(userEntity.getUsername()).
                age(user.getAge()).
                email(user.getEmail()).
                build();
    }

public GetUserDTO getUser(String username) throws UserNotFoundException {
    if (!(userRepository.existsByUsername(username))) {
        throw new UserNotFoundException("Пользователь с username-ом " + username + " не найден");
    }

    Optional<UserEntity> userOptional = userRepository.findByUsername(username);

    if (userOptional.isPresent()) {
        UserEntity user = userOptional.get();
        List<GetArticleDTO> articles = articleService.getAllArticles(username);

        return GetUserDTO.builder()
                .username(user.getUsername())
                .age(user.getAge())
                .email(user.getEmail())
                .articles(articles)
                .build();
    } else {
        throw new UserNotFoundException("Пользователь с username-ом " + username + " не найден");
    }
}


    public List<GetUserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        List<GetUserDTO> users = new ArrayList<>();

        for (UserEntity user: userEntities) {
            users.add(GetUserDTO.builder().
                    username(user.getUsername()).
                    age(user.getAge()).
                    email(user.getEmail()).
                    articles(articleService.getAllArticles(user.getUsername())).
                    build());
        }

        return users;
    }

    public UserEntity updateUsername(Long id, String username) throws UserNotFoundException, UserAlreadyExistException {
        if (!(userRepository.existsById(id))) {
            throw new UserAlreadyExistException("Пользователь с id: " + id + " не существует");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("Пользователь под таким username-ом уже существует");
        }

        Optional<UserEntity> user = userRepository.findById(id);

        return userRepository.save(UserEntity.builder().
                id(id).
                username(username).
                password(user.get().getPassword()).
                age(user.get().getAge()).
                email(user.get().getEmail()).
                build());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
