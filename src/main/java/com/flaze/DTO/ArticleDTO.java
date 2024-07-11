package com.flaze.DTO;

import com.flaze.entity.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {

    private String title;
    private String description;
    private String text;

    public static ArticleDTO toModel(Optional<ArticleEntity> article) {
        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setTitle(article.get().getTitle());
        articleDTO.setDescription(article.get().getDescription());
        articleDTO.setText(article.get().getText());

        return articleDTO;
    }

}
