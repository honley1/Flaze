package com.flaze.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDTO { // DTO для того чтобы получать пользователя по его username-у без пароля

    private Long id;
    private String username;
    private Integer age;
    private String email;
    private List<GetArticleDTO> articles;

    public GetUserDTO(String username, Integer age, String email, List<GetArticleDTO> articles) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.articles = articles;
    }

    public GetUserDTO(String username, Integer age, String email) {
        this.username = username;
        this.age = age;
        this.email = email;
    }
}
