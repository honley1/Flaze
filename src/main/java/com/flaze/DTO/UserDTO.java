package com.flaze.DTO;

import com.flaze.entity.ArticleEntity;
import com.flaze.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String password;
    private String confirmPassword;
    private Integer age;
    private String email;
}
