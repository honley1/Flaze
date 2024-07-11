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
public class GetArticleDTO { // DTO для того чтобы получать статью по ее id

    private Long id;
    private String title;
    private String description;
    private String text;
    private Long authorId;

    public static GetArticleDTO toModel(Optional<ArticleEntity> article) {
        GetArticleDTO getArticleDTO = new GetArticleDTO();

        getArticleDTO.setId(article.get().getId());
        getArticleDTO.setTitle(article.get().getTitle());
        getArticleDTO.setDescription(article.get().getDescription());
        getArticleDTO.setText(article.get().getText());

        return getArticleDTO;
    }

}
